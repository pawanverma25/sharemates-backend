package dev.pawan.sharemate.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsDTO {

	Integer id;
	BigDecimal Amount;
}
