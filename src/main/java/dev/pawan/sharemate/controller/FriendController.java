package dev.pawan.sharemate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.pawan.sharemate.model.Friend;
import dev.pawan.sharemate.request.FriendRequestDTO;
import dev.pawan.sharemate.request.SettleExpenseRequestDTO;
import dev.pawan.sharemate.response.FriendDTO;
import dev.pawan.sharemate.service.FriendService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/getFriends/{userId}")
    public ResponseEntity<List<FriendDTO>> getFriendsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getFriendsByUserId(userId));
    }

    @GetMapping("/getFriendRequestList/{userId}")
    public ResponseEntity<List<FriendDTO>> getFriendRequestListByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getFriendRequestListByUserId(userId));
    }

    @PostMapping("/addFriend/")
    public ResponseEntity<Friend> addFriend(@RequestBody FriendRequestDTO friendRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.addFriend(friendRequest));
    }
    
    @PostMapping("/updateFriendRequest/")
    public ResponseEntity<Friend> updateFriendRequest(@RequestBody FriendRequestDTO friendRequest) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(friendService.updateFriendRequest(friendRequest));
	}

    @GetMapping("/searchFriends/{userId}/{searchQuery}")
    public ResponseEntity<List<FriendDTO>> searchFriend(@PathVariable String searchQuery,
            @PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.searchFriends(searchQuery, userId));
    }
    
    @PostMapping("/settleFriendExpenses/")
    public ResponseEntity<Boolean> settleFriendExpenses(@RequestBody SettleExpenseRequestDTO settleExpenseRequestDTO) {
		Boolean result = friendService.settleFriendExpenses(settleExpenseRequestDTO.getUserId(), settleExpenseRequestDTO.getFriendId());
		return ResponseEntity.status(HttpStatus.OK).body(result);	
    }

}
