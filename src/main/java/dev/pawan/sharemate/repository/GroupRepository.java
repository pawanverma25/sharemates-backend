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
                SUM(es.amountOwed),
                g.createdDate)
            FROM ExpenseSplit es
            JOIN Expense e on es.expenseId = e.id
            JOIN Group g on e.groupId = g.id
            JOIN GroupMember gm ON gm.groupId = g.id AND gm.userId = :userId
            WHERE es.userId = :userId
            GROUP BY g.id, g.name, g.description, g.createdBy, g.createdDate
            ORDER BY g.name DESC
            """)
    public List<GroupDTO> getGroupsByUserId(int userId);
}
