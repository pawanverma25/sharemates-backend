package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.response.UserDTO;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

	@Query("""
			SELECT new dev.pawan.sharemate.response.UserDTO(u.id, u.name, u.uid, u.email)
			FROM Friend f
			JOIN User u on f.friendId = u.id
			and u.id = :userId
			order by f.status, u.name
	""")
	public List<UserDTO> getFriendsByUserId(int userId);

}
