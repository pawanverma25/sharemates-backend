package dev.pawan.sharemate.model;

import dev.pawan.sharemate.enums.NotificationTemplateCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NOTIFICATION_TEMPLATE")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "USER_ID", nullable = false)
	private Long userId;
	
	@Column(name = "TEMPLATE_CODE", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationTemplateCode templateCode;
	
	@Column(name = "MESSAGE", nullable = false)
	private String message;
}
