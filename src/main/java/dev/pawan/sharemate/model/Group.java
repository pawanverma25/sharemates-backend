package dev.pawan.sharemate.model;

import java.time.LocalDateTime;

import dev.pawan.sharemate.request.GroupRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GROUP_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    
    public Group(GroupRequestDTO groupRequest) {
		this.name = groupRequest.getName();
		this.description = groupRequest.getDescription();
		this.createdBy = groupRequest.getCreatedBy();		
	}
}
