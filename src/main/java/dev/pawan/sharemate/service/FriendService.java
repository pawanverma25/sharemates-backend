package dev.pawan.sharemate.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BalanceService balanceService;
    private final ExpenseService expenseService;

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
        friend.setStatus(friendRequest.getStatus());
        return friendRepository.save(friend);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public Friend updateFriendRequest(FriendRequestDTO friendRequest) {
        int userId = friendRequest.getUserId();
        int friendId = friendRequest.getFriendId();
        FriendStatus status = friendRequest.getStatus();

        Friend directFriend = friendRepository.findByUserIdAndFriendId(userId, friendId);
        Friend reverseFriend = friendRepository.findByUserIdAndFriendId(friendId, userId);

        if (directFriend == null) {
            directFriend = new Friend();
            directFriend.setUserId(userId);
            directFriend.setFriendId(friendId);
            directFriend.setStatus(status);
            directFriend.setCreatedAt(LocalDateTime.now());
        }
        reverseFriend.setModifiedAt(LocalDateTime.now());
        reverseFriend.setStatus(status);
        friendRepository.saveAll(List.of(directFriend, reverseFriend));
        balanceService.createBalances(friendRequest.getUserId(), friendRequest.getFriendId());
        return directFriend;
    }
    
    public List<FriendDTO> searchFriends(String searchQuery, int userId) {
        List<FriendDTO> friends = friendRepository.findAllByUsernameOrEmail(searchQuery, userId);
        return friends;
    }

    @Transactional(rollbackFor = {IllegalStateException.class})
	public Boolean settleFriendExpenses(Integer userId, Integer friendId) {
		Boolean isBalanceSettled = balanceService.settleBalance(userId, friendId);
		Boolean isExpenseSettled = expenseService.settleExpensesByUserIdAndPaidBy(userId, friendId);
		if (isBalanceSettled && isExpenseSettled) {
			return true;
		} else {
			throw new IllegalStateException("Failed to settle expenses or balances for user " + userId + " and friend " + friendId);
		}	
    }

}
