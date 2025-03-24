package dev.pawan.sharemate.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.enums.VerificationStatus;
import dev.pawan.sharemate.model.EmailDetails;
import dev.pawan.sharemate.model.EmailVerification;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.EmailVerificationRepo;
import dev.pawan.sharemate.repository.UserRepository;
import dev.pawan.sharemate.service.EmailService;
import dev.pawan.sharemate.util.ConstantsUtil;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepo emailVerificationRepo;

    @Autowired
    private UserRepository userRepo;

    public VerificationStatus sendVerificationEmail(User user) {
        String verificationCode = "" + (int)(Math.random()* 1e6);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(60);
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(verificationCode);
        emailVerification.setUser(user);
        emailVerification.setExpiryTime(expiryTime);
        emailVerification = emailVerificationRepo.save(emailVerification);
        return emailService.sendSimpleMail(new EmailDetails(user.getEmail(),
                "Verify your email id and be a Sharemate",
                MessageFormat.format(
                        "Hi {0}, \n\nHere is your verification code.\n\n Code - {1}\n\n\n Thanks for choosing us,\nRegards,\n\n Sharemate Team",
                        user.getName(), verificationCode),
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
}
