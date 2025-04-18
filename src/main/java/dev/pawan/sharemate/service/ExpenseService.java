package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.enums.SplitType;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.repository.ExpenseRepository;
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
    
 public Expense saveExpense(ExpenseRequestDTO request) {
	 
	    String desc = request.getDescription();
		LocalDateTime date = request.getCreatedDate();
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
			savedExpense = expenseRepo.save(exp);	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return savedExpense;
		
    	
	}

}
