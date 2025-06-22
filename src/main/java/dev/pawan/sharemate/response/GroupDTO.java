package dev.pawan.sharemate.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.pawan.sharemate.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer createdBy;
    private BigDecimal amountOwed;
    private LocalDateTime createdDate;
    
    public GroupDTO(Group group) {
		this.id = group.getId();
		this.name = group.getName();
		this.description = group.getDescription();
		this.createdBy = group.getCreatedBy();
		this.createdDate = group.getCreatedDate();
	}
}
