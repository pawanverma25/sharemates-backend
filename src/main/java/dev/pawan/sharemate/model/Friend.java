package dev.pawan.sharemate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import dev.pawan.sharemate.enums.FriendStatus;

@Entity
@Table(name = "FRIENDS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
