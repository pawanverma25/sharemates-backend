package dev.pawan.sharemate.controller;

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
import dev.pawan.sharemate.model.ExponentPushToken;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.request.EmailVerificationRequest;
import dev.pawan.sharemate.request.LoginRequest;
import dev.pawan.sharemate.request.RegisterRequest;
import dev.pawan.sharemate.request.UserDTO;
import dev.pawan.sharemate.response.AuthResponseDTO;
import dev.pawan.sharemate.service.EmailVerificationService;
import dev.pawan.sharemate.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailVerificationService emailVerificationTokenService;

    @GetMapping("/emailexists/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExistance(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("exists", userService.checkEmailExistance(email)));
    }

    @GetMapping("/usernameexists/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExistance(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("exists", userService.checkUsernameExistance(username)));
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Map<String, Boolean>> updateUser(@RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("updated", userService.updateUser(user)));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequest request) {
        AuthResponseDTO response = null;
        try {
            response = userService.registerUser(request);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new AuthResponseDTO("Registration failed: " + e.getMessage(), null, null, null, null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest request) {
        AuthResponseDTO response = null;
        try {
            Optional<String> token = userService.verify(request.getEmail(), request.getPassword());
            if (token.isPresent()) {
                User savedUser = userService.getUserDetails(request.getEmail());
                response = new AuthResponseDTO("Login successful",
                        UserMapper.INSTANCE.toDto(savedUser), token.get(), 60000, savedUser.getEmailVerified());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new AuthResponseDTO("Registration failed: " + e.getMessage(), null, null, null, null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/sendVerificationEmail/{userId}")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required");
        }
        if (userId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User ID");
        }
        try {
            User user = userService.getUserDetails(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            emailVerificationTokenService.sendVerificationEmail(user);
            return ResponseEntity.status(HttpStatus.OK).body("Verification email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email");
        }
    }

    @PostMapping("/verifyEmail/")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequest request) {
        String verificationCode = request.getVerificationCode();
        Integer userId = request.getUserId();

        if (verificationCode == null || verificationCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code is required");
        }

        try {
            boolean isVerified = emailVerificationTokenService.verifyEmail(userId, verificationCode);
            if (isVerified) {
                return ResponseEntity.status(HttpStatus.OK).body("Email verified successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify email");
        }
    }

    @GetMapping("/getUserPreferences/{userId}")
    public ResponseEntity<Map<String, String>> getUserPreferences(@PathVariable Integer userId) {
		if (userId == null || userId <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid User ID"));
		}
		try {
			Map<String, String> preferences = userService.getUserPreferences(userId);
			return ResponseEntity.status(HttpStatus.OK).body(preferences);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to get preferences: " + e.getMessage()));
		}
	}
    
    @PostMapping("/updateUserPreferences/{userId}")
    public ResponseEntity<Map<String, String>> updateUserPreferences(@PathVariable Integer userId,
			@RequestBody Map<String, String> preferences) {
		if (userId == null || userId <= 0) {
			return ResponseEntity.status(HttpStatus.BPAD_REQUEST).body(Map.of("error", "Invalid User ID"));
		}
		try {
			Map<String, String> updatedPreferences = userService.updateUserPreferences(userId, preferences);
			return ResponseEntity.status(HttpStatus.OK).body(updatedPreferences);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to update preferences: " + e.getMessage()));
		}
	}
    
    @PostMapping("/updateExpoToken")
    public ResponseEntity<Boolean> updateExpoToken(@RequestBody ExponentPushToken request) {
		try {
			boolean status = userService.updateExpoToken(request);
			return ResponseEntity.status(HttpStatus.OK).body(status);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
