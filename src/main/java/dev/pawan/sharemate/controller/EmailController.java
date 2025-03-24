package dev.pawan.sharemate.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000/")
public class EmailController {

    // @Autowired
    // private EmailService emailService;

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
}
