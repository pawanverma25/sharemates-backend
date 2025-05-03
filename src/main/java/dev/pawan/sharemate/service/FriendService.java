package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.FriendRepository;
import dev.pawan.sharemate.repository.UserRepository;
import dev.pawan.sharemate.request.UserDTO;
import dev.pawan.sharemate.response.FriendDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;


    public List<FriendDTO> getFriendsByUserId(int userId) {
        return friendRepository.getFriendsByUserId(userId);
    }

	public Friend addFriend(Friend friend) {
		return friendRepository.save(friend);
		
	}

	public UserDTO searchFriend(String username) { 
		UserDTO userDTO = new UserDTO();
		User user = userRepository.findByUsername(username);
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setName(user.getName());
		return userDTO;
	}

}
 