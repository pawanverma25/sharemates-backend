package dev.pawan.sharemate.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
	List<Balance> findByUserId(Integer userId);
	
	Balance findByUserIdAndFriendId(Integer userId,Integer friendId);

	@Modifying
    @Transactional
    @Query("UPDATE Balance SET amount = amount + :amount WHERE user.id = :paidBy and friend.id = :borrowedBy")
	void updateAmount(@Param("paidBy") Integer paidBy,@Param("borrowedBy")  Integer borrowedBy,@Param("amount")  BigDecimal amount);


}
