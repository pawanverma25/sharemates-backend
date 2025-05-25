package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.model.User;
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
    
    public void createExpense(ParticipantsDTO participant,Expense exp) {
    	ExpenseSplit expenseSplit = new ExpenseSplit();
        expenseSplit.setExpenseId(exp.getId());
        expenseSplit.setUserId(participant.getId());
        expenseSplit.setAmountOwed(participant.getAmount());
        expenseSplit.setPaid(exp.getPaidBy() == participant.getId() ? 'Y' : 'N');
        expenseSplitRepo.save(expenseSplit);
    }
    
    public void settleBalancesBetweenUserAndFriend(ParticipantsDTO participant,Expense exp) {
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

    public Boolean saveExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
        for (ParticipantsDTO participant : participants) {
            try {
            	createExpense(participant,exp);
                Balance count = balanceRepository.findByUserIdAndFriendId(exp.getPaidBy(), participant.getId());
                if (count != null) {
                    balanceRepository.updateAmount(exp.getPaidBy(), participant.getId(),participant.getAmount());
                    balanceRepository.updateAmount(participant.getId(), exp.getPaidBy(),participant.getAmount().multiply(BigDecimal.valueOf(-1)));
                } else if (exp.getPaidBy() != participant.getId()) {
                	settleBalancesBetweenUserAndFriend(participant,exp);
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
	        	createExpense(participant,exp);
	        	settleBalancesBetweenUserAndFriend(participant,exp);
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
            expenseSplitRepo.deleteByUserIdAndExpenseId(p.getId(),exp.getId());
		}
		addFriendtoExpenseSplit(participants,exp);
		return Boolean.TRUE;
	}

	@Transactional
	public Boolean updateExpenses(ExpenseRequestDTO request) {
		Expense exp = expenseService.updateExpense(request);
    	System.out.println("expense is saved " + exp);
		List<ParticipantsDTO> participants = request.getParticipants();
    	Integer expenseId = request.getExpenseId();
		List<ParticipantsDTO> existingParticipants = expenseSplitRepository.getExistingParticipants(expenseId);
		if(participants.size()>existingParticipants.size()) {
			System.out.println("Addition of Friend in expense");
			 addFriendtoExpenseSplit(participants,exp);
		}else if(participants.size()<existingParticipants.size()) {
			System.out.println("Removal of Friend in expense");
			 removeFriendtoExpenseSplit(participants,existingParticipants,exp);
		}else {
			System.out.println("Expense edit of Friend");
			addFriendtoExpenseSplit(participants,exp);
		}
		return Boolean.TRUE;
		
	}

}
