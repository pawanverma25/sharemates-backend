package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.response.ExpenseDTO;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("""
            		SELECT new dev.pawan.sharemate.response.ExpenseDTO(e.id, g.id, g.name,
            		new dev.pawan.sharemate.response.UserDTO(u1.id, u1.name, u1.uid, u1.email),
            		e.description, e.amount, e.createdDate, e.modifiedDate, e.splitType, es.amountOwed,
            		new dev.pawan.sharemate.response.UserDTO(u2.id, u2.name, u2.uid, u2.email), e.expenseCategory)
            		FROM Expense e
            		JOIN User u1 on e.createdBy = u1.id
            		JOIN User u2 on e.paidBy = u2.id
            		LEFT JOIN Group g on g.id = e.groupId
            		JOIN ExpenseSplit es on es.expenseId = e.id
            		and es.userId = :userId order by e.modifiedDate desc
            """)
    List<ExpenseDTO> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);
}
