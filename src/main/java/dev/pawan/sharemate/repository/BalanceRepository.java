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
import dev.pawan.sharemate.response.BalanceDTO;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
	
	@Query("SELECT new dev.pawan.sharemate.response.BalanceDTO(b.friendId, u.name, b.amount) FROM Balance b join User u on u.id=b.friendId WHERE b.userId = :userId")
	List<BalanceDTO> getBalancesByUserId( @Param("userId")Integer userId);

	Balance findByUserIdAndFriendId(Integer userId, Integer friendId);

	@Modifying
	@Transactional
	@Query("UPDATE Balance SET amount = amount + :amount WHERE userId = :paidBy and friendId = :borrowedBy")
	void updateAmount(@Param("paidBy") Integer paidBy, @Param("borrowedBy") Integer borrowedBy,
			@Param("amount") BigDecimal amount);

}
