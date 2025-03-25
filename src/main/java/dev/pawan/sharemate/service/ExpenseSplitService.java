package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.response.ExpenseSplitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseSplitService {
	
	private final ExpenseSplitRepository expenseSplitRepo; 
	
	public List<ExpenseSplitDTO> getExpenseSplit(Integer expenseId) {
		return expenseSplitRepo.getExpenseSplit(expenseId);
	}
	
}
