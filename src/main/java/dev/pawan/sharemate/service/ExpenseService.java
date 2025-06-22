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

        String desc = request.getDescription();
        Date date = request.getCreatedDate();
        Integer paidBy = request.getPaidBy();
        Integer groupId = request.getGroupId();
        Integer createdBy = request.getCreatedBy();
        BigDecimal totalAmount = request.getAmount();
        SplitType splitType = request.getSplitType();
        Expense savedExpense = new Expense();
        try {
            Expense exp = new Expense();
            exp.setAmount(totalAmount);
            exp.setCreatedBy(createdBy);
            exp.setCreatedDate(date);
            exp.setDescription(desc);
            exp.setPaidBy(paidBy);
            exp.setSplitType(splitType);
            exp.setGroupId(groupId);
            exp.setExpenseCategory(category);
            savedExpense = expenseRepo.save(exp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedExpense;

    }

	public Expense updateExpense(ExpenseRequestDTO request,String category) {
		Integer expenseId = request.getExpenseId();
		String desc = request.getDescription();
        Date date = request.getCreatedDate();
        Integer paidBy = request.getPaidBy();
        Integer groupId = request.getGroupId();
        Integer createdBy = request.getCreatedBy();
        BigDecimal totalAmount = request.getAmount();
        SplitType splitType = request.getSplitType();
        Expense savedExpense = new Expense();
        try {
            Expense exp = new Expense();
            exp.setId(expenseId);
            exp.setAmount(totalAmount);
            exp.setCreatedBy(createdBy);
            exp.setCreatedDate(date);
            exp.setDescription(desc);
            exp.setPaidBy(paidBy);
            exp.setSplitType(splitType);
            exp.setGroupId(groupId);
            exp.setExpenseCategory(category);
            savedExpense = expenseRepo.save(exp);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return savedExpense;
		
	}
	

}
