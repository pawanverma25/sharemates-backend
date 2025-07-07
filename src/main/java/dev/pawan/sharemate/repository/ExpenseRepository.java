package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dev.pawan.sharemate.model.Expense;
import dev.pawan.sharemate.response.ExpenseDTO;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query("""
                SELECT distinct new dev.pawan.sharemate.response.ExpenseDTO(
                    e.id, g.id, g.name,
                    new dev.pawan.sharemate.response.UserDTO(u1.id, u1.name, u1.username, u1.email),
                    e.description, e.amount, e.createdDate, e.modifiedDate, e.splitType,
                    CASE WHEN e.paidBy = es.userId THEN es.amountOwed - e.amount ELSE es.amountOwed - es.amountPaid END,
                    new dev.pawan.sharemate.response.UserDTO(u2.id, u2.name, u2.username, u2.email),
                    e.expenseCategory
                )
                FROM Expense e
                JOIN ExpenseSplit es ON es.expenseId = e.id
                JOIN User u1 ON e.createdBy = u1.id
                JOIN User u2 ON e.paidBy = u2.id
                LEFT JOIN Group g ON g.id = e.groupId
                WHERE es.userId = :userId and e.isActive = 'Y'
                ORDER BY e.modifiedDate DESC
            """)
    List<ExpenseDTO> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("""
				SELECT distinct e.id FROM Expense e
				JOIN ExpenseSplit es ON es.expenseId = e.id
				WHERE es.userId = :userId and e.isActive = 'Y'
				and e.paidBy = :paidBy and es.userId != e.paidBy 
				and es.amountOwed > es.amountPaid
			""")
	List<Integer> findByUserIdAndPaidBy(@Param("userId") Integer userId, @Param("paidBy") Integer paidBy);
    
    @Modifying
    @Transactional
    @Query("""
        UPDATE ExpenseSplit es
        SET es.amountPaid = es.amountOwed
        WHERE es.userId = :userId 
        AND es.userId != (
            SELECT e.paidBy FROM Expense e WHERE e.id = es.expenseId
        )
        AND es.amountOwed > es.amountPaid
        AND es.expenseId IN (
            SELECT e.id FROM Expense e WHERE e.isActive = 'Y' AND e.paidBy = :paidBy
        )
    """)
    int settleExpenseSplitsUserIdAndPaidBy(@Param("userId") Integer userId, @Param("paidBy") Integer paidBy);
}
