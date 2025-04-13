package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.response.FriendDTO;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    @Query("""
            		SELECT new dev.pawan.sharemate.response.FriendDTO(u.id, u.name, u.uid, u.email, f.status, b.amount)
            		FROM Friend f
            		JOIN User u on f.friendId = u.id
                    JOIN Balance b on b.user.id = :userId and b.friend.id = u.id
            		and f.userId = :userId
            		order by f.status, u.name
            """)
    public List<FriendDTO> getFriendsByUserId(int userId);

}
