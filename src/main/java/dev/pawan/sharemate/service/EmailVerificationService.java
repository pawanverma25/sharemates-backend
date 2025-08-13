package dev.pawan.sharemate.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.enums.VerificationStatus;
import dev.pawan.sharemate.model.EmailDetails;
import dev.pawan.sharemate.model.EmailVerification;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.EmailVerificationRepository;
import dev.pawan.sharemate.repository.UserRepository;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepo;

    @Autowired
    private UserRepository userRepo;

    public VerificationStatus sendVerificationEmail(User user) {
        String verificationCode = String.format("%06d", (int) (Math.random() * 1_000_000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(60);
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(verificationCode);
        emailVerification.setUser(user);
        emailVerification.setExpiryTime(expiryTime);
        emailVerification = emailVerificationRepo.save(emailVerification);
        return emailService.sendSimpleMail(new EmailDetails(user.getEmail(),
                "Your Sharemate Verification Code: " + verificationCode,
                "<!DOCTYPE html>\r\n" +
                        "<html lang=\"en\">\r\n" +
                        "<head>\r\n" +
                        "<meta charset=\"UTF-8\"/>\r\n" +
                        "<meta name=\"viewport\"content=\"width=device-width, initial-scale=1\"/>\r\n" +
                        "<title>OTP Verification</title>\r\n" +
                        "</head>\r\n" +
                        "<body style=\"margin: 0; padding: 0;font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #111111;\">\r\n"
                        +
                        "<table width=\"100%\"cellpadding=\"0\"cellspacing=\"0\"style=\"padding: 40px 0;\">\r\n"
                        +
                        "<tr>\r\n" +
                        "<td align=\"center\">\r\n" +
                        "<table width=\"520\"cellpadding=\"0\"cellspacing=\"0\"style=\"background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); border: 1px solid #d1d1d1; padding: 40px;\">\r\n"
                        +
                        "<tr>\r\n" +
                        "<td style=\"text-align: left;\">\r\n" +
                        "<h1 style=\"font-size: 24px; margin: 0 0 24px; color: #111111;\">Hello " + user.getName()
                        + ",</h1>\r\n" +
                        "<p style=\"font-size: 16px; line-height: 1.75; color: #444444; margin: 0 0 28px;\">\r\n" +
                        "We received a request to verify your account. Please use the following code to proceed:\r\n" +
                        "</p>\r\n" +
                        "<p style=\"display: inline-block; font-size: 30px; letter-spacing: 6px; font-weight: bold; background-color: #f0f9f6; padding: 16px 32px; border-radius: 10px; color: #00a86b; margin: 28px 0;\">\r\n"
                        + verificationCode + "\r\n" +
                        "</p>\r\n" +
                        "<p style=\"font-size: 16px; line-height: 1.75; color: #444444; margin: 28px 0 0;\">\r\n" +
                        "This code is valid for a short time and should not be shared with anyone. If you did not request this verification, please ignore this message.\r\n"
                        +
                        "</p>\r\n" +
                        "<div style=\"font-size: 14px; color: #444444; text-align: center; margin-top: 40px; border-top: 1px solid #d1d1d1; padding-top: 20px;\">\r\n"
                        +
                        "&copy; " + new SimpleDateFormat("yyyy").format(new Date())
                        + " Sharemate Team. All rights reserved.\r\n" +
                        "</div>\r\n" +
                        "</td>\r\n" +
                        "</tr>\r\n" +
                        "</table>\r\n" +
                        "</td>\r\n" +
                        "</tr>\r\n" +
                        "</table>\r\n" +
                        "</body>\r\n" +
                        "</html>",
                true,
                null));
    }

    public VerificationStatus validateVerificationCode(String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepo.findByVerificationCode(verificationCode);
        if (emailVerification == null)
            return VerificationStatus.INVALID;
        else if (emailVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            return VerificationStatus.EXPIRED;
        }

        User user = emailVerification.getUser();
        user.setEmailVerified('Y');
        userRepo.save(user);

        emailVerificationRepo.delete(emailVerification);
        return VerificationStatus.VERIFIED;
    }

    public boolean verifyEmail(Integer userId, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepo.findByUserIdAndVerificationCode(userId,
                verificationCode);
        if (emailVerification != null) {
            LocalDateTime expiryTime = emailVerification.getExpiryTime();
            if (expiryTime.isAfter(LocalDateTime.now())) {
                User user = emailVerification.getUser();
                user.setEmailVerified('Y');
                userRepo.save(user);
                emailVerificationRepo.delete(emailVerification);
                return true;
            }
        }
        return false;
    }
}