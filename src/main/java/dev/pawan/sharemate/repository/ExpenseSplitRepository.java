package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.response.ExpenseSplitDTO;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Integer>{
	
	@Query("SELECT new dev.pawan.sharemate.response.ExpenseSplitDTO(es.id, es.expenseId, "
			+ "new dev.pawan.sharemate.response.UserDTO(u.id, u.name, u.uid, u.email), "
			+ "es.amountOwed, es.paid) "
			+ "FROM ExpenseSplit es "
			+ "JOIN User u on es.userId = u.id "
			+ "and es.expenseId = :expenseId")
	public List<ExpenseSplitDTO> getExpenseSplit(@Param("expenseId") Integer expenseId);
}
