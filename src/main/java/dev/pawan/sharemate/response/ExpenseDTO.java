package dev.pawan.sharemate.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.pawan.sharemate.enums.SplitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Integer id;
    private Integer groupId;
    private String groupName;
    private UserDTO createdBy;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private SplitType splitType;
    private BigDecimal amountOwed;
    private UserDTO paidBy;
    private String expenseCategory;
}
