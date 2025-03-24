package dev.pawan.sharemate.request;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class RegisterRequest {
	private final String name;
	private final String email;
	private final String password;
}
