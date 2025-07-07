package dev.pawan.sharemate.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BALANCES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "USER_ID", nullable = false)
    private int userId;

//    @ManyToOne
//    @JoinColumn(name = "friend_id", nullable = false)
    @Column(name = "FRIEND_ID", nullable = false)
    private int friendId;

    @Column(nullable = false)
    private BigDecimal amount;
    
    public Balance(int userId, int friendId, BigDecimal amount) {
		this.userId = userId;
		this.friendId = friendId;
		this.amount = amount;
	}
}
