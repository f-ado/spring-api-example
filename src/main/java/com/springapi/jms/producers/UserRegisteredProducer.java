package com.springapi.jms.producers;

import com.springapi.jms.messages.UserRegisteredMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisteredProducer.class);

    @Autowired
    private final JmsTemplate jmsTemplate;

    @Value("${destination.user.registration}")
    private String destination;

    public UserRegisteredProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(UserRegisteredMessage message) {
        jmsTemplate.convertAndSend(destination, message);
        LOGGER.info("{}:\n{}", this.getClass().getName(), message);
    }
}
