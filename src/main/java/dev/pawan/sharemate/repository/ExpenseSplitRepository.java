package dev.pawan.sharemate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.ExpenseSplit;
import dev.pawan.sharemate.request.ParticipantsDTO;
import dev.pawan.sharemate.response.ExpenseSplitDTO;


@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Integer> {
 
	Optional<ExpenseSplit> findByUserIdAndExpenseId(Integer userId,Integer expenseId);
	
    @Query("""
		SELECT new dev.pawan.sharemate.response.ExpenseSplitDTO(es.id, es.expenseId, 
        new dev.pawan.sharemate.response.UserDTO(u.id, u.name, u.username, u.email), 
        es.amountOwed, es.amountPaid) 
        FROM ExpenseSplit es 
        JOIN User u on es.userId = u.id 
        and es.expenseId = :expenseId
    """)
    public List<ExpenseSplitDTO> getExpenseSplit(@Param("expenseId") Integer expenseId);

    
    @Query("""
		SELECT new dev.pawan.sharemate.request.ParticipantsDTO(es.userId,es.amountOwed)
		FROM ExpenseSplit es
		where es.expenseId = :expenseId
	""")
	public List<ParticipantsDTO> getExistingParticipants(@Param("expenseId") Integer expenseId);

	void deleteByUserIdAndExpenseId(Integer id, Integer id2);
}
