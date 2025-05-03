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
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.request.UserDTO;
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
    
    @PostMapping("/addFriend")
    public ResponseEntity<Friend> asddFriend(@RequestBody Friend friend) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.addFriend(friend));
    }
    
    @GetMapping("/searchFriend/{username}")
    public ResponseEntity<UserDTO> searchFriend(@PathVariable String username){
    	return ResponseEntity.status(HttpStatus.OK).body(friendService.searchFriend(username));
    }
    
}
