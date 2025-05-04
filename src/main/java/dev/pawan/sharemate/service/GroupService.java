package dev.pawan.sharemate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.model.Group;
import dev.pawan.sharemate.model.GroupMember;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.GroupMemberRepository;
import dev.pawan.sharemate.repository.GroupRepository;
import dev.pawan.sharemate.request.AddMemberRequestDTO;
import dev.pawan.sharemate.request.GroupRequestDTO;
import dev.pawan.sharemate.response.GroupDTO;
import dev.pawan.sharemate.response.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
	
	private final GroupRepository groupRepository;
	private final GroupMemberRepository groupMemberRepository; 
	
	public List<GroupDTO> getGroupsByUserId(int userId) {
		return groupRepository.getGroupsByUserId(userId);
	}

	public List<UserDTO> getGroupMembersByGroupId(int groupId) {
		return groupMemberRepository.getGroupMembersByGroupId(groupId);
	}

	public Group createGroup(Group groupRequest) {
		return groupRepository.save(groupRequest);
	}

	public Object addMembersToGroup(AddMemberRequestDTO addMemberRequestDTO) {
		List<Integer> friendList = addMemberRequestDTO.getFriendList();
		for(int i=0;i<friendList.size();i++) {
			GroupMember groupMember = new GroupMember();
			groupMember.setGroupId(addMemberRequestDTO.getGroupId());
			groupMember.setUserId(friendList.get(i));
			groupMemberRepository.save(groupMember);
		}
		Map<String,String> res = new HashMap<>();
		res.put("status", "200");
		res.put("message", "All friends added to group");
		
		return res;
	}

	public Boolean updateGroup(GroupRequestDTO groupRequestDTO) {
		Optional<Group> foundGroup = groupRepository.findById(groupRequestDTO.getId());
		if(foundGroup.isPresent()) {
			Group existingGroup = foundGroup.get();
			if(groupRequestDTO.getName()!=null)
			 existingGroup.setName(groupRequestDTO.getName());
			if(groupRequestDTO.getDescription()!=null)
				existingGroup.setDescription(groupRequestDTO.getDescription());
			groupRepository.save(existingGroup);
			return true;
		}else {
			return false;
		}	
	}

}
