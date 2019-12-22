package com.springapi.service;

import com.springapi.jms.messages.UserRegisteredMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(final UserRegisteredMessage userRegisteredMessage) {

        String emailTextMessage = String.format(
            "Dear %s %s, welcome! To confirm your account, please click the link below:\n %s/confirm?token=%s",
            userRegisteredMessage.getFirstName(),
            userRegisteredMessage.getLastName(),
            userRegisteredMessage.getAppUrl(),
            userRegisteredMessage.getConfirmationToken()
        );

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(userRegisteredMessage.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText(emailTextMessage);
        registrationEmail.setFrom("noreply@test.com");
        registrationEmail.setReplyTo("noreply@test.com");

        LOGGER.debug("Mail sent to user={}", userRegisteredMessage.getEmail());
        mailSender.send(registrationEmail);
    }
}
