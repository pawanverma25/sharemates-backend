package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.repository.BalanceRepository;
import dev.pawan.sharemate.repository.ExpenseRepository;
import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.response.ExpenseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
	private final ExpenseRepository expenseRepo;
	private final ExpenseSplitRepository expenseSplitRepository;
	private final BalanceRepository balanceRepository;

	public List<ExpenseDTO> getExpensesByUserId(Integer userId, Pageable pageable) {
		return expenseRepo.findAllByUserId(userId, pageable);
	}

	public Expense saveExpense(ExpenseRequestDTO request, String category) {
		Expense exp = new Expense(request);
		exp.setExpenseCategory(category);
		return expenseRepo.save(exp);

	}

	public Expense updateExpense(ExpenseRequestDTO request, String category) {
		Expense exp = new Expense(request);
		exp.setExpenseCategory(category);
		return expenseRepo.save(exp);
	}

	// TODO: Implement logic to settle expense
	@Transactional
	public Boolean deleteExpense(Integer expenseId) {
		Expense expense = expenseRepo.findById(expenseId)
				.orElseThrow(() -> new IllegalStateException("Expense with id " + expenseId + " does not exist"));

		expense.setIsActive('N');
		return true;
	}

	@Transactional
	public boolean settleExpenseById(Integer expenseId, Integer userId) {
		
		ExpenseSplit split = expenseSplitRepository
				.findByUserIdAndExpenseId(userId, expenseId)
				.orElseThrow(() -> new IllegalStateException("Expense split not found for user " + userId
						+ " and expense " + expenseId));
		// Settle the expense by marking it as paid
		BigDecimal remainingAmountPaid = split.getAmountOwed().subtract(split.getAmountPaid());
		split.setAmountPaid(remainingAmountPaid);
		expenseSplitRepository.save(split);

		Expense expense = expenseRepo.findById(expenseId).orElseThrow(
				() -> new IllegalStateException("Expense with id " + expenseId + " does not exist"));
		Integer paidBy = expense.getPaidBy();

		Balance balanceUser = balanceRepository.findByUserIdAndFriendId(userId, paidBy);
		Balance balanceFriend = balanceRepository.findByUserIdAndFriendId(paidBy, userId);

		balanceUser.setAmount(balanceUser.getAmount().add(remainingAmountPaid));
		balanceFriend.setAmount(balanceFriend.getAmount().subtract(remainingAmountPaid));
		balanceRepository.saveAll(List.of(balanceUser, balanceFriend));

		return true;
	}

	public List<Integer> getExpenseIdsByUserIdAndPaidBy(Integer userId, Integer PaidBy) {
		List<Integer> expenseIds = expenseRepo.findByUserIdAndPaidBy(userId, PaidBy);
		return expenseIds;
	}
	
	public Boolean settleExpensesByUserIdAndPaidBy(Integer userId, Integer PaidBy) {
		try {
			int updatedRows = expenseRepo.settleExpenseSplitsUserIdAndPaidBy(userId, PaidBy);
			return updatedRows > 0;
		} catch (Exception e) {
			throw new IllegalStateException("Failed to settle expenses for user " + userId + " and paid by " + PaidBy, e);
		}
	}

}
