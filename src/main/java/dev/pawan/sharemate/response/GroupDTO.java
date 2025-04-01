package dev.pawan.sharemate.response;

import java.time.LocalDateTime;

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
    private UserDTO createdBy;
    private LocalDateTime createdDate;
}
