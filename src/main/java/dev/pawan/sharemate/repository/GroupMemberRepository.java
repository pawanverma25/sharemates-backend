package dev.pawan.sharemate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.GroupMember;
import dev.pawan.sharemate.response.UserDTO;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
	@Query("""
			select new dev.pawan.sharemate.response.UserDTO(u.id, u.name, u.uid, u.email)
			From GroupMember gm
			Join User u
			on u.id = gm.userId and gm.groupId =:groupId
			""")
	public List<UserDTO> getGroupMembersByGroupId(@Param("groupId") int groupId);
}
