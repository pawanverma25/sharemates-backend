package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.repository.FriendRepository;
import dev.pawan.sharemate.response.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final FriendRepository friendRepository;
	
	
	public List<UserDTO> getFriendsByUserId(int userId) {
		return friendRepository.getFriendsByUserId(userId);
	}

}
