package dev.pawan.sharemate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.pawan.sharemate.response.UserDTO;
import dev.pawan.sharemate.service.FriendService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/")
public class FriendController {
	
	private final FriendService friendService;
	
    @GetMapping("/getFriends/{userId}")
    public ResponseEntity<List<UserDTO>> getFriendsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getFriendsByUserId(userId));
    }
}
