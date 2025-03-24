package dev.pawan.sharemate.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.mapper.UserMapper;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.request.LoginRequest;
import dev.pawan.sharemate.request.RegisterRequest;
import dev.pawan.sharemate.response.AuthResponseDTO;
import dev.pawan.sharemate.service.EmailVerificationService;
import dev.pawan.sharemate.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailVerificationService emailVerificationTokenService;

    @GetMapping("/exists/{username}")
    public ResponseEntity<Map<String, Boolean>> checkExistance(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new HashMap<String, Boolean>() {
            {
                put("exists", userService.checkExistance(email));
            }
        });
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequest request) {
        AuthResponseDTO response = null;
		try {
			response = userService.registerUser(request);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	                new AuthResponseDTO("Registration failed: " + e.getMessage(), null, null, null));
		}
		return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest request) {
        Optional<String> token = userService.verify(request.getEmail(), request.getPassword());
        if (token.isPresent()) {
            User savedUser = userService.getUserDetails(request.getEmail());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new AuthResponseDTO("Login successful",
                            UserMapper.INSTANCE.toDto(savedUser), token.get(), 60000));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
