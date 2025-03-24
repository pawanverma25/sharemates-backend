package dev.pawan.sharemate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.pawan.sharemate.model.EmailVerification;

public interface EmailVerificationRepo extends JpaRepository<EmailVerification, Integer> {
	public EmailVerification findByVerificationCode(String verificationCode);
}
