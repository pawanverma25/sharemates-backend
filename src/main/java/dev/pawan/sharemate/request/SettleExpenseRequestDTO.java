package dev.pawan.sharemate.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettleExpenseRequestDTO {
	private Integer expenseId;
	private Integer userId;
	private String amountPaid;
	private Integer friendId;
}
