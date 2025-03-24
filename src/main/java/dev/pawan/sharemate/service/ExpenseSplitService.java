package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.response.ExpenseSplitDTO;

@Service
public class ExpenseSplitService {
	
	@Autowired
	private ExpenseSplitRepository expenseSplitRepo; 
	
	public List<ExpenseSplitDTO> getExpenseSplit(Integer expenseId) {
		return expenseSplitRepo.getExpenseSplit(expenseId);
	}
	
}
