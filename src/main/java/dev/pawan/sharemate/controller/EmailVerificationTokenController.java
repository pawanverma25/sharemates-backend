package dev.pawan.sharemate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.enums.VerificationStatus;
import dev.pawan.sharemate.service.EmailVerificationService;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000/")
public class EmailVerificationTokenController {

    @Autowired
    private EmailVerificationService emailVerificationTokenService;

    // @PostMapping("/sendMail")
    // public Boolean sendMail(@RequestBody EmailDetails details) {
    // Boolean status = emailService.sendSimpleMail(details);
    // return status;
    // }

    // @PostMapping("/sendMailWithAttachment")
    // public Boolean sendMailWithAttachment(
    // @RequestBody EmailDetails details) {
    // Boolean status = emailService.sendMailWithAttachment(details);
    // return status;
    // }

    @GetMapping("/email/verification")
    public ResponseEntity<VerificationStatus> getMethodName(String verificationCode) {
		return ResponseEntity.status(HttpStatus.OK).body(emailVerificationTokenService.validateVerificationCode(verificationCode));
    }

}
