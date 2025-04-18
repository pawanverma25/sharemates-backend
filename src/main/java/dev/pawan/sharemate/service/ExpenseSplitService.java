package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.List;

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

	public void saveExpenseSplit(List<ParticipantsDTO> participants, Expense exp) {
		Integer expenseId = exp.getId();
		for(int i=0;i<participants.size();i++) {
			try {
			ExpenseSplit expenseSplit = new ExpenseSplit();
			expenseSplit.setExpenseId(expenseId);
			expenseSplit.setUserId(participants.get(i).getId());
			expenseSplit.setAmountOwed(participants.get(i).getAmount());
			expenseSplit.setPaid('N');
			expenseSplitRepo.save(expenseSplit);
			Balance count = balanceRepository.findByUserIdAndFriendId(exp.getPaidBy(),participants.get(i).getId());
			if(count!=null) {
				balanceRepository.updateAmount(exp.getPaidBy(),participants.get(i).getId(),participants.get(i).getAmount());
				balanceRepository.updateAmount(participants.get(i).getId(),exp.getPaidBy(),participants.get(i).getAmount().multiply(BigDecimal.valueOf(-1)));
			}else if(exp.getPaidBy() !=participants.get(i).getId()){
				Balance betweenUserAndFriend = new Balance();
				Balance betweenFriendAndUser = new Balance();
				User user = new User();
				User friend = new User();
				user.setId(exp.getPaidBy());
				friend.setId(participants.get(i).getId());
				betweenUserAndFriend.setUser(user);
				betweenUserAndFriend.setFriend(friend);
				betweenUserAndFriend.setAmount(participants.get(i).getAmount());
				balanceRepository.save(betweenUserAndFriend);
				user.setId(participants.get(i).getId());
				friend.setId(exp.getPaidBy());
				betweenFriendAndUser.setUser(user);
				betweenFriendAndUser.setFriend(friend);
				betweenFriendAndUser.setAmount(participants.get(i).getAmount().multiply(BigDecimal.valueOf(-1)));
				balanceRepository.save(betweenFriendAndUser);
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
