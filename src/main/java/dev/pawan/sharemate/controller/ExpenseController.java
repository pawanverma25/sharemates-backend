package dev.pawan.sharemate.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.repository.ExpenseSplitRepository;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.response.ExpenseDTO;
import dev.pawan.sharemate.service.ExpenseService;
import dev.pawan.sharemate.service.ExpenseSplitService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseSplitService expenseSplitService;
    private final ExpenseSplitRepository expenseSplitRepository;

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@RequestParam Integer userId,
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByUserId(userId, pageable));
    }

    @PostMapping("/addExpenses")
    public ResponseEntity<Boolean> addExpenses(@RequestBody ExpenseRequestDTO request) {
        // saving in expense table
        try {
            Expense exp = expenseService.saveExpense(request);
            System.out.println("expense is saved " + exp);
            List<ParticipantsDTO> participants = request.getParticipants();
            Boolean response = expenseSplitService.saveExpenseSplit(participants, exp);
            System.out.println("expense split is saved " + exp);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
    }
    
    @PostMapping("/editExpenses")
    public ResponseEntity<Boolean> updateExpense(@RequestBody ExpenseRequestDTO request){
    	Expense exp = expenseService.updateExpense(request);
    	System.out.println("expense is saved " + exp);
    	List<ParticipantsDTO> participants = request.getParticipants();
    	Integer expenseId = request.getExpenseId();
		List<ParticipantsDTO> existingParticipants = expenseSplitRepository.getExistingParticipants(expenseId);
		Boolean response = Boolean.FALSE;
		if(participants.size()>existingParticipants.size()) {
			System.out.println("Addition of Friend in expense");
			 response = expenseSplitService.addFriendtoExpenseSplit(participants,exp);
		}else if(participants.size()<existingParticipants.size()) {
			System.out.println("Removal of Friend in expense");
			 response = expenseSplitService.removeFriendtoExpenseSplit(participants,existingParticipants,exp);
		}else {
			System.out.println("Expense edit of Friend");
			 response = expenseSplitService.addFriendtoExpenseSplit(participants,exp);
		}
		if(response) return ResponseEntity.status(HttpStatus.OK).body(response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
		
    }
}
