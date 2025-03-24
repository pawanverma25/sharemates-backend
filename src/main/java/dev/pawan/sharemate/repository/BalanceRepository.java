package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
	List<Balance> findByUserId(Integer userId);

}
