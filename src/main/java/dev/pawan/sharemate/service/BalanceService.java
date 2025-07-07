package dev.pawan.sharemate.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.repository.BalanceRepository;
import dev.pawan.sharemate.response.BalanceDTO;

@Service
public class BalanceService {

	@Autowired
	private BalanceRepository balanceRepo;

	public List<BalanceDTO> getBalances(Integer userId) {
		List<BalanceDTO> balances = balanceRepo.getBalancesByUserId(userId);
		return balances;
	}
	
	@Transactional
	public void createBalances(int userId1, int userId2) {
		Balance balance1 = new Balance(userId1, userId2, BigDecimal.ZERO);
		Balance balance2 = new Balance(userId2, userId1, BigDecimal.ZERO);
		balanceRepo.saveAll(List.of(balance1, balance2));
	}
	
	@Transactional
	public Boolean settleBalance(int userId1, int userId2) {
		return balanceRepo.settleBalance(userId1, userId2) == 2;
	}
}
