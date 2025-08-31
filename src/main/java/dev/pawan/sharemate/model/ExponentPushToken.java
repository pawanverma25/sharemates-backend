package dev.pawan.sharemate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "EXPONENT_PUSH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
public class ExponentPushToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_id", nullable = false, unique = true)
	private Integer userId;
	
	@Column(name = "token", nullable = false)
	private String pushToken;
}
