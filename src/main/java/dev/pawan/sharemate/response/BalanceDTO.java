package dev.pawan.sharemate.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
	private Integer id;
	private Integer friendId;
	private String friendName;
	private Double amount;
}
