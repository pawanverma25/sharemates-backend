package dev.pawan.sharemate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.response.ExpenseDTO;
import dev.pawan.sharemate.service.ExpenseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;

	@GetMapping("/expenses")
	public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@RequestParam Integer userId, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(expenseService.getExpensesByUserId(userId, pageable));
	}
}
