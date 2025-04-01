package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.GroupMemberRepository;
import dev.pawan.sharemate.repository.GroupRepository;
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

}
