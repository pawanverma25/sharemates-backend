package dev.pawan.sharemate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import dev.pawan.sharemate.enums.VerificationStatus;

@Entity
@Table(name = "EMAIL_VERIFICATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;
}
