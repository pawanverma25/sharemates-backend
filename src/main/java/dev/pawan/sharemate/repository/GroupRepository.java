package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.pawan.sharemate.model.Group;
import dev.pawan.sharemate.response.ExpenseDTO;
import dev.pawan.sharemate.response.GroupDTO;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("""
				SELECT new dev.pawan.sharemate.response.GroupDTO(
				    g.id,
				    g.name,
				    g.description,
					new dev.pawan.sharemate.response.UserDTO(uc.id, uc.name, uc.username, uc.email),
				    g.createdDate,
				    COALESCE(SUM(case when es.amountOwed > 0 then es.amountOwed else 0 end), 0) as positiveBalance,
				    COALESCE(SUM(case when es.amountOwed < 0 then es.amountOwed else 0 end), 0) as negativeBalance
				)
				FROM Group g
    			JOIN User uc ON uc.id = g.createdBy
				JOIN GroupMember gm 
				    ON gm.groupId = g.id 
				   AND gm.userId = :userId
				LEFT JOIN Expense e 
				    ON e.groupId = g.id
				LEFT JOIN ExpenseSplit es 
				    ON es.expenseId = e.id
				   AND es.userId = :userId
				GROUP BY g.id, g.name, g.description, g.createdBy, g.createdDate
				ORDER BY g.name DESC
            """)
    public List<GroupDTO> getGroupsByUserId(int userId);

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
	        WHERE e.isActive = 'Y' and e.groupId = :groupId
	        ORDER BY e.modifiedDate DESC
		""")
	public List<ExpenseDTO> getGroupExpenses(Integer groupId);

}
