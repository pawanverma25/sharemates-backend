package dev.pawan.sharemate.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.config.UserPrincipal;
import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.request.SettleExpenseRequestDTO;
import dev.pawan.sharemate.response.ExpenseDTO;
import dev.pawan.sharemate.service.ExpenseCategoryService;
import dev.pawan.sharemate.service.ExpenseService;
import dev.pawan.sharemate.service.ExpenseSplitService;
import dev.pawan.sharemate.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;
	private final ExpenseSplitService expenseSplitService;
	private final HuggingFaceService huggingFaceService;
	private final ExpenseCategoryService expenseCategoryService;

	@GetMapping("/expenses")
	public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@RequestParam Integer userId,
			@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
		SecurityContext context = SecurityContextHolder.getContext();
		UserPrincipal user = (UserPrincipal) context.getAuthentication().getPrincipal();
	    if(user.getUserId() != userId) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	    }
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByUserId(userId, pageable));
	}

	@Transactional
	@PostMapping("/addExpenses")
	public ResponseEntity<Expense> addExpenses(@RequestBody ExpenseRequestDTO request) {
		// saving in expense table
		Expense exp = new Expense();
		try {
			List<String> expcategory = expenseCategoryService.getCategories();
			String category = huggingFaceService.huggingFaceAPICall(request.getDescription(), expcategory);
			exp = expenseService.saveExpense(request, category);

			List<ParticipantsDTO> participants = request.getParticipants();
			expenseSplitService.saveExpenseSplit(participants, exp);

			return ResponseEntity.status(HttpStatus.OK).body(exp);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp);

	}

	@Transactional
	@PostMapping("/editExpenses")
	public ResponseEntity<Boolean> updateExpense(@RequestBody ExpenseRequestDTO request) {
		List<String> expcategory = expenseCategoryService.getCategories();
		String category = huggingFaceService.huggingFaceAPICall(request.getDescription(), expcategory);
		Boolean response = expenseSplitService.updateExpenses(request, category);
		if (response)
			return ResponseEntity.status(HttpStatus.OK).body(response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);

	}

	@DeleteMapping("/deleteExpense/{expenseId}")
	public ResponseEntity<Boolean> deleteExpense(@RequestParam Integer expenseId) {
		Boolean response = expenseService.deleteExpense(expenseId);
		if (response) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
	}
	
	@Transactional
	@PostMapping("/settleExpense/")
	public ResponseEntity<Boolean> settleExpenseById(@RequestBody SettleExpenseRequestDTO request) {
		Boolean response = expenseService.settleExpenseById(request.getExpenseId(), request.getUserId());
		if (response) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
	}
}
