package dev.pawan.sharemate.request;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import dev.pawan.sharemate.enums.SplitType;
import lombok.Data;

@Data
public class ExpenseRequestDTO {
	
	Integer expenseId;
    String description;
    Date createdDate;
    Integer paidBy;
    Integer groupId;
    Integer createdBy;
    BigDecimal amount;
    SplitType splitType;
    List<ParticipantsDTO> participants;
}
