package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.ExpenseRepository;
import dev.pawan.sharemate.response.ExpenseDTO;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;
	
	public List<ExpenseDTO> getExpensesByUserId(Integer userId, Pageable pageable){
		return expenseRepo.findAllByUserId(userId, pageable);
	}
	
}
