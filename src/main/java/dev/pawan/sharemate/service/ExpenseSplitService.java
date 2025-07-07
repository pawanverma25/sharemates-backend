package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.repository.BalanceRepository;
import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.response.ExpenseSplitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseSplitService {

	private final ExpenseSplitRepository expenseSplitRepo;

	private final BalanceRepository balanceRepository;
	private final ExpenseSplitRepository expenseSplitRepository;
	private final ExpenseService expenseService;

	public List<ExpenseSplitDTO> getExpenseSplit(Integer expenseId) {
		return expenseSplitRepo.getExpenseSplit(expenseId);
	}

	public void createExpense(ParticipantsDTO participant, Expense exp) {
		ExpenseSplit expenseSplit = new ExpenseSplit();
		expenseSplit.setExpenseId(exp.getId());
		expenseSplit.setUserId(participant.getId());
		expenseSplit.setAmountOwed(participant.getAmount());
		expenseSplit.setAmountPaid(exp.getPaidBy() == participant.getId() ? participant.getAmount() : BigDecimal.ZERO);
		expenseSplitRepo.save(expenseSplit);
	}

	public void settleBalancesBetweenUserAndFriend(ParticipantsDTO participant, Expense exp) {
		Balance betweenUserAndFriend = new Balance(exp.getPaidBy(), participant.getId(), participant.getAmount());
		Balance betweenFriendAndUser = new Balance(participant.getId(), exp.getPaidBy(),
				participant.getAmount().multiply(BigDecimal.valueOf(-1)));
		balanceRepository.saveAll(List.of(betweenUserAndFriend, betweenFriendAndUser));
	}

	public Boolean saveExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
		for (ParticipantsDTO participant : participants) {
			try {
				createExpense(participant, exp);
				Balance count = balanceRepository.findByUserIdAndFriendId(exp.getPaidBy(), participant.getId());
				if (count != null) {
					balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(), participant.getAmount());
					balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),
							participant.getAmount().multiply(BigDecimal.valueOf(-1)));
				} else if (exp.getPaidBy() != participant.getId()) {
					settleBalancesBetweenUserAndFriend(participant, exp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	public Boolean addFriendtoExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
		for (ParticipantsDTO participant : participants) {
			Optional<ExpenseSplit> optionalEntity = expenseSplitRepo.findByUserIdAndExpenseId(participant.getId(),
					exp.getId());
			if (optionalEntity.isPresent()) {
				ExpenseSplit expenseSplit = optionalEntity.get();
				BigDecimal previousAmount = expenseSplit.getAmountOwed();
				BigDecimal newAmount = participant.getAmount();
				expenseSplit.setAmountOwed(participant.getAmount());
				BigDecimal settleAmt = previousAmount.subtract(newAmount);
				expenseSplitRepo.save(expenseSplit);
				balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(),
						settleAmt.multiply(BigDecimal.valueOf(-1)));
				balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(), settleAmt);
			} else {
				createExpense(participant, exp);
				Balance count = balanceRepository.findByUserIdAndFriendId(exp.getPaidBy(), participant.getId());
				if (count != null) {
					balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(), participant.getAmount());
					balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),
							participant.getAmount().multiply(BigDecimal.valueOf(-1)));
				} else if (exp.getPaidBy() != participant.getId()) {
					settleBalancesBetweenUserAndFriend(participant, exp);
				}
			}
		}
		return Boolean.TRUE;
	}

	public Boolean removeFriendtoExpenseSplit(List<ParticipantsDTO> participants,
			List<ParticipantsDTO> existingParticipants, Expense exp) {
		Set<Integer> idsInParticipants = participants.stream().map(ParticipantsDTO::getId).collect(Collectors.toSet());
		List<ParticipantsDTO> diffId = existingParticipants.stream()
				.filter(obj -> !idsInParticipants.contains(obj.getId())).collect(Collectors.toList());
		for (ParticipantsDTO p : diffId) {
			BigDecimal previousAmount = p.getAmount();
			balanceRepository.updateAmount(exp.getPaidBy(), p.getId(), previousAmount.multiply(BigDecimal.valueOf(-1)));
			balanceRepository.updateAmount(p.getId(), exp.getPaidBy(), previousAmount);
			expenseSplitRepo.deleteByUserIdAndExpenseId(p.getId(), exp.getId());
		}
		return Boolean.TRUE;
	}

	@Transactional
	public Boolean updateExpenses(ExpenseRequestDTO request, String category) {
		Expense exp = expenseService.updateExpense(request, category);
		List<ParticipantsDTO> participants = request.getParticipants();
		Integer expenseId = request.getExpenseId();
		List<ParticipantsDTO> existingParticipants = expenseSplitRepository.getExistingParticipants(expenseId);
		addFriendtoExpenseSplit(participants, exp);
		removeFriendtoExpenseSplit(participants, existingParticipants, exp);
		return Boolean.TRUE;
	}

}
