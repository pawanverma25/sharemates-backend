package dev.pawan.sharemate.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private Integer friendId;
    private String friendName;
    private BigDecimal amount;
}
