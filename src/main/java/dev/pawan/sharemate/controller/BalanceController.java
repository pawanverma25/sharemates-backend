package dev.pawan.sharemate.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.response.BalanceDTO;
import dev.pawan.sharemate.service.BalanceService;

@RestController
@RequestMapping("/api")
public class BalanceController {    
	
	private Logger logger = LoggerFactory.getLogger(BalanceController.class);
	
	@Autowired
	private BalanceService balanceService;
	
	@GetMapping("/getBalance/{userId}")
	public ResponseEntity<List<BalanceDTO>> getBalanace(@PathVariable Integer userId){
		try{
			List<BalanceDTO> res = balanceService.getBalances(userId);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch(Exception e) {
			logger.error("Error while fetching balances: " + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
