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

import dev.pawan.sharemate.response.ExpenseSplitDTO;
import dev.pawan.sharemate.service.ExpenseSplitService;

@RestController
@RequestMapping("/api/")
public class ExpenseSplitController {
	
	@Autowired
	private ExpenseSplitService expenseSplitService;
	
	@GetMapping("/expenses-splits/{expenseId}")
	public ResponseEntity<List<ExpenseSplitDTO>> getExpensesSplits(@PathVariable Integer expenseId){
		return ResponseEntity.status(HttpStatus.OK).body(expenseSplitService.getExpenseSplit(expenseId));
	}
}
