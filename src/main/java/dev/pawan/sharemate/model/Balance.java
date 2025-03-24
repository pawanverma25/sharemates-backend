package dev.pawan.sharemate.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "BALANCES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Column(nullable = false)
    private Double amount;
}
