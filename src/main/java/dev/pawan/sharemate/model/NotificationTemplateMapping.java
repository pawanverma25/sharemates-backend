package dev.pawan.sharemate.model;

import java.util.Map;

import dev.pawan.sharemate.enums.NotificationTemplateCode;
import dev.pawan.sharemate.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NOTIFICATION_TEMPLATE")
public class NotificationTemplateMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "TEMPLATE_CODE", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationTemplateCode templateCode;
	
	
    @Column(name = "MESSAGE_TEMPLATE", columnDefinition = "json", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, String> messageTemplate;
}

