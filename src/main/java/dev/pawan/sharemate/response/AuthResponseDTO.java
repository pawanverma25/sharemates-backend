package dev.pawan.sharemate.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String message;
    private UserDTO user;
    private String authorization;
    private Integer expiresIn;
    private Character emailVerified;
}
