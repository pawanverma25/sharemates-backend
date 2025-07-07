package dev.pawan.sharemate.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.repository.ExpCategoryRepository;
import dev.pawan.sharemate.request.ExpenseRequestDTO;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.request.SettleExpenseRequestDTO;
import dev.pawan.sharemate.response.ExpenseDTO;
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
	private final ExpCategoryRepository expCategoryRepository;
	private final HuggingFaceService huggingFaceService;

	@GetMapping("/expenses")
	public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@RequestParam Integer userId,
			@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByUserId(userId, pageable));
	}

	@PostMapping("/addExpenses")
	public ResponseEntity<Expense> addExpenses(@RequestBody ExpenseRequestDTO request) {
		// saving in expense table
		Expense exp = new Expense();
		try {
			List<String> expcategory = expCategoryRepository.getAllCategory();
			String category = huggingFaceService.huggingFaceAPICall(request.getDescription(), expcategory);
			exp = expenseService.saveExpense(request, category);
			System.out.println("expense is saved " + exp);
			List<ParticipantsDTO> participants = request.getParticipants();
			Boolean response = expenseSplitService.saveExpenseSplit(participants, exp);
			System.out.println("expense split is saved " + exp);
			return ResponseEntity.status(HttpStatus.OK).body(exp);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp);

	}

	@PostMapping("/editExpenses")
	public ResponseEntity<Boolean> updateExpense(@RequestBody ExpenseRequestDTO request) {
		List<String> expcategory = expCategoryRepository.getAllCategory();
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
	
	@PostMapping("/settleExpense/")
	public ResponseEntity<Boolean> settleExpenseById(@RequestBody SettleExpenseRequestDTO request) {
		Boolean response = expenseService.settleExpenseById(request.getExpenseId(), request.getUserId());
		if (response) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
	}
}
