package dev.pawan.sharemate.request;

import lombok.Data;

@Data
public class UserDTO {

	private Integer id;
	private String username;
	private String email;
	private String name;
}
