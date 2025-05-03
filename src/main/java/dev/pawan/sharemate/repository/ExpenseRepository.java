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
                SELECT distinct new dev.pawan.sharemate.response.ExpenseDTO(
                    e.id, g.id, g.name,
                    new dev.pawan.sharemate.response.UserDTO(u1.id, u1.name, u1.username, u1.email),
                    e.description, e.amount, e.createdDate, e.modifiedDate, e.splitType,
                    CASE WHEN e.paidBy = es.userId THEN es.amountOwed - e.amount ELSE es.amountOwed END,
                    new dev.pawan.sharemate.response.UserDTO(u2.id, u2.name, u2.username, u2.email),
                    e.expenseCategory
                )
                FROM Expense e
                JOIN ExpenseSplit es ON es.expenseId = e.id
                JOIN User u1 ON e.createdBy = u1.id
                JOIN User u2 ON e.paidBy = u2.id
                LEFT JOIN Group g ON g.id = e.groupId
                WHERE es.userId = :userId
                ORDER BY e.modifiedDate DESC
            """)
    List<ExpenseDTO> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);
}
