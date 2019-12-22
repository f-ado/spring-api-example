package com.springapi.jms.listeners;

import com.springapi.jms.messages.UserRegisteredMessage;
import com.springapi.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisteredListener.class);

    private EmailService emailService;

    @Autowired
    public UserRegisteredListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = "${destination.user.registration}")
    public void listen(
        @Payload UserRegisteredMessage userRegisteredMessage,
        @Headers MessageHeaders headers,
        Message message
    ) {
        LOGGER.info("{}:\n{}", this.getClass().getName(), userRegisteredMessage);
        emailService.sendEmail(userRegisteredMessage);
    }
}
