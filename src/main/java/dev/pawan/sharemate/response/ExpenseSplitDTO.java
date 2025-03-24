package dev.pawan.sharemate.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSplitDTO {
    private Integer id;
    private Integer expenseId;
    private UserDTO user;
    private BigDecimal amountOwed;
    private Character paid; 
}
