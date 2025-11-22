package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.pawan.sharemate.model.Group;
import dev.pawan.sharemate.response.GroupDTO;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("""
				SELECT new dev.pawan.sharemate.response.GroupDTO(
				    g.id,
				    g.name,
				    g.description,
				    g.createdBy,
				    COALESCE(SUM(es.amountOwed), 0) as totalOwed,
				    g.createdDate
				)
				FROM Group g
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
}
