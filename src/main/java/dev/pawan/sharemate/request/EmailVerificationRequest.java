package dev.pawan.sharemate.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class EmailVerificationRequest {
    private String verificationCode; // The verification code sent to the user's email
    private Integer userId; // The ID of the user to verify

}
