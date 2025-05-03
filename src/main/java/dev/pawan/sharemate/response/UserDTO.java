package dev.pawan.sharemate.response;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserDTO {
    private Integer id;
    private String name;
    private String username;
    private String email;
}
