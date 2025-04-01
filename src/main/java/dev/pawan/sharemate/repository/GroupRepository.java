package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.pawan.sharemate.model.Group;
import dev.pawan.sharemate.response.GroupDTO;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	@Query("""
			SELECT new dev.pawan.sharemate.response.GroupDTO(g.id, g.name, g.description, 
			new dev.pawan.sharemate.response.UserDTO(u.id, u.name, u.uid, u.email),
			g.createdDate)
			FROM Group g
			JOIN User u on g.createdBy = u.id
			JOIN GroupMember gm on gm.userId = :userId
			and gm.groupId = g.id
			order by g.name desc
			""")
	public List<GroupDTO> getGroupsByUserId(int userId);
}
