package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.enums.FriendStatus;
import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.repository.FriendRepository;
import dev.pawan.sharemate.request.FriendRequestDTO;
import dev.pawan.sharemate.response.FriendDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public List<FriendDTO> getFriendsByUserId(int userId) {
        return friendRepository.getFriendsByUserId(userId);
    }

    public List<FriendDTO> getFriendRequestListByUserId(int userId) {
        return friendRepository.getFriendRequestListByUserId(userId);
    }

    public Friend addFriend(FriendRequestDTO friendRequest) {
        Friend friend = new Friend();
        friend.setUserId(friendRequest.getUserId());
        friend.setFriendId(friendRequest.getFriendId());
        friend.setStatus(FriendStatus.PENDING);
        return friendRepository.save(friend);

    }

    public List<FriendDTO> searchFriends(String searchQuery, int userId) {
        List<FriendDTO> friends = friendRepository.findAllByUsernameOrEmail(searchQuery, userId);
        return friends;
    }

}
