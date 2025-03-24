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
public class ExpenseDetailsDTO {
    private Integer id;
    private String expenseUid;
    private Integer groupId;
    private String groupName;
    private UserDTO createdBy;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private SplitType splitType;
    private UserDTO paidBy;
    private String expenseCategory;
}
