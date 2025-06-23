package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.enums.SplitType;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.model.ExpenseCategory;
import dev.pawan.sharemate.repository.ExpenseRepository;
import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.response.ExpenseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepo;

    public List<ExpenseDTO> getExpensesByUserId(Integer userId, Pageable pageable) {
        return expenseRepo.findAllByUserId(userId, pageable);
    }

    public Expense saveExpense(ExpenseRequestDTO request,String category) {
        Expense exp = new Expense(request);
        exp.setExpenseCategory(category);
        return expenseRepo.save(exp);

    }

	public Expense updateExpense(ExpenseRequestDTO request,String category) {
        Expense exp = new Expense(request);
        exp.setExpenseCategory(category);
        return expenseRepo.save(exp);
	}
	

}
