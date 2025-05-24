package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.BalanceRepository;
import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.response.ExpenseSplitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseSplitService {

    private final ExpenseSplitRepository expenseSplitRepo;

    private final BalanceRepository balanceRepository;

    public List<ExpenseSplitDTO> getExpenseSplit(Integer expenseId) {
        return expenseSplitRepo.getExpenseSplit(expenseId);
    }

    public void saveExpenseSplit(List<ParticipantsDTO> participants) {

    }

    public Boolean saveExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
        Integer expenseId = exp.getId();
        for (ParticipantsDTO participant : participants) {
            try {
                ExpenseSplit expenseSplit = new ExpenseSplit();
                expenseSplit.setExpenseId(expenseId);
                expenseSplit.setUserId(participant.getId());
                expenseSplit.setAmountOwed(participant.getAmount());
                expenseSplit.setPaid(exp.getPaidBy() == participant.getId() ? 'Y' : 'N');
                expenseSplitRepo.save(expenseSplit);
                Balance count = balanceRepository.findByUserIdAndFriendId(exp.getPaidBy(), participant.getId());
                if (count != null) {
                    balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(),
                            participant.getAmount());
                    balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),
                            participant.getAmount().multiply(BigDecimal.valueOf(-1)));
                } else if (exp.getPaidBy() != participant.getId()) {
                    Balance betweenUserAndFriend = new Balance();
                    Balance betweenFriendAndUser = new Balance();
                    User user = new User();
                    User friend = new User();
                    user.setId(exp.getPaidBy());
                    friend.setId(participant.getId());
                    betweenUserAndFriend.setUser(user);
                    betweenUserAndFriend.setFriend(friend);
                    betweenUserAndFriend.setAmount(participant.getAmount());
                    balanceRepository.save(betweenUserAndFriend);
                    user.setId(participant.getId());
                    friend.setId(exp.getPaidBy());
                    betweenFriendAndUser.setUser(user);
                    betweenFriendAndUser.setFriend(friend);
                    betweenFriendAndUser.setAmount(participant.getAmount().multiply(BigDecimal.valueOf(-1)));
                    balanceRepository.save(betweenFriendAndUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

	public Boolean addFriendtoExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
		for(ParticipantsDTO participant: participants) {
			Optional<ExpenseSplit> optionalEntity = expenseSplitRepo.findByUserIdAndExpenseId(participant.getId(),exp.getId());
			if (optionalEntity.isPresent()) {
				ExpenseSplit expenseSplit =  optionalEntity.get();
				BigDecimal previousAmount = expenseSplit.getAmountOwed();
				BigDecimal newAmount = participant.getAmount();
				expenseSplit.setAmountOwed(participant.getAmount());
				BigDecimal settleAmt = previousAmount.subtract(newAmount);
				expenseSplitRepo.save(expenseSplit);
				balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(),settleAmt.multiply(BigDecimal.valueOf(-1)));
                balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),settleAmt);
	        } else {
	        	ExpenseSplit expenseSplit = new ExpenseSplit();
                expenseSplit.setExpenseId(exp.getId());
                expenseSplit.setUserId(participant.getId());
                expenseSplit.setAmountOwed(participant.getAmount());
                expenseSplit.setPaid(exp.getPaidBy() == participant.getId() ? 'Y' : 'N');
                expenseSplitRepo.save(expenseSplit);
                Balance betweenUserAndFriend = new Balance();
                Balance betweenFriendAndUser = new Balance();
                User user = new User();
                User friend = new User();
                user.setId(exp.getPaidBy());
                friend.setId(participant.getId());
                betweenUserAndFriend.setUser(user);
                betweenUserAndFriend.setFriend(friend);
                betweenUserAndFriend.setAmount(participant.getAmount());
                balanceRepository.save(betweenUserAndFriend);
                user.setId(participant.getId());
                friend.setId(exp.getPaidBy());
                betweenFriendAndUser.setUser(user);
                betweenFriendAndUser.setFriend(friend);
                betweenFriendAndUser.setAmount(participant.getAmount().multiply(BigDecimal.valueOf(-1)));
                balanceRepository.save(betweenFriendAndUser);
	        }
		}
		return Boolean.TRUE;
	}

	public Boolean removeFriendtoExpenseSplit(List<ParticipantsDTO> participants,List<ParticipantsDTO> existingParticipants, Expense exp) {
		Set<Integer> idsInParticipants = participants.stream().map(ParticipantsDTO::getId).collect(Collectors.toSet());
		List<ParticipantsDTO> diffId = existingParticipants.stream().filter(obj -> !idsInParticipants.contains(obj.getId())).collect(Collectors.toList());
		for(ParticipantsDTO p : diffId) {
			BigDecimal previousAmount = p.getAmount();
			balanceRepository.updateAmount(exp.getPaidBy(), p.getId(),previousAmount.multiply(BigDecimal.valueOf(-1)));
            balanceRepository.updateAmount(p.getId(), exp.getPaidBy(),previousAmount);
			expenseSplitRepo.deleteByExpenseIdAndUserId(exp.getId(), p.getId());
		}
		for(ParticipantsDTO participant : participants){
			Optional<ExpenseSplit> optionalEntity = expenseSplitRepo.findByUserIdAndExpenseId(participant.getId(),exp.getId());
			if (optionalEntity.isPresent()) {
				ExpenseSplit expenseSplit =  optionalEntity.get();
				BigDecimal previousAmount = expenseSplit.getAmountOwed();
				BigDecimal newAmount = participant.getAmount();
				expenseSplit.setAmountOwed(participant.getAmount());
				BigDecimal settleAmt = previousAmount.subtract(newAmount);
				expenseSplitRepo.save(expenseSplit);
				balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(),settleAmt.multiply(BigDecimal.valueOf(-1)));
                balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),settleAmt);
	        }
		}
		
		return Boolean.TRUE;
	}

}
