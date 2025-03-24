package dev.pawan.sharemate.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.model.Balance;
import dev.pawan.sharemate.repository.BalanceRepository;
import dev.pawan.sharemate.response.BalanceDTO;

@Service
public class BalanceService {

	@Autowired
	private BalanceRepository balanceRepo;

	public List<BalanceDTO> getBalances(Integer userId) {
		List<Balance> balances = balanceRepo.findByUserId(userId);

		return balances.stream().map(balance -> {
			return new BalanceDTO(balance.getId(), balance.getFriend().getId(), balance.getFriend().getName(),
					balance.getAmount());
		}).collect(Collectors.toList());
	}
}
