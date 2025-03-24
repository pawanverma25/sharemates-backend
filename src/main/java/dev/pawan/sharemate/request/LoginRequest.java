package dev.pawan.sharemate.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	private final String email;
	private final String password;
}
