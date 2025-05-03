package dev.pawan.sharemate.response;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import dev.pawan.sharemate.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class FriendDTO {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private FriendStatus status;
    private BigDecimal balance;
}