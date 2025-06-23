package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.response.FriendDTO;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    @Query("""
                SELECT new dev.pawan.sharemate.response.FriendDTO(u.id, u.name, u.username, u.email, f.status, b.amount)
                FROM Friend f
                JOIN User u on f.friendId = u.id
                JOIN Balance b on b.userId = :userId and b.friendId = u.id
                and f.userId = :userId
                order by f.status, u.name
            """)
    public List<FriendDTO> getFriendsByUserId(int userId);

    @Query("""
                SELECT new dev.pawan.sharemate.response.FriendDTO(u.id, u.name, u.username, u.email, f.status, null)
                FROM Friend f
                JOIN User u on f.userId = u.id and f.friendId = :userId and f.status = 'PENDING'
                where u.id != :userId
                order by f.status desc, f.createdAt desc
            """)
    public List<FriendDTO> getFriendRequestListByUserId(@Param("userId") int userId);

    @Query("""
                SELECT new dev.pawan.sharemate.response.FriendDTO(u.id, u.name, u.username, u.email, f.status, null)
                FROM User u
                LEFT JOIN Friend f on f.friendId = u.id and f.userId = :userId
                where u.id != :userId and (u.username like concat("%", :searchQuery, "%") or u.email like concat("%", :searchQuery, "%"))
                order by f.status desc, u.name
            """)
    public List<FriendDTO> findAllByUsernameOrEmail(@Param("searchQuery") String searchQuery,
            @Param("userId") int userId);
    
    public Friend findByUserIdAndFriendId(int userId, int friendId);

}
