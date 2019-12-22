package com.springapi.jms.messages;

import com.springapi.domain.User;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserRegisteredMessage implements Serializable {

    private static final long serialVersionUID = -155419968330137687L;

    private UUID uuid;
    private String appUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String confirmationToken;

    public UserRegisteredMessage() {
        // Nothing to do here
    }

    public UserRegisteredMessage(UUID uuid, String appUrl, User user) {
        this.uuid = uuid;
        this.appUrl = appUrl;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.confirmationToken = user.getConfirmationToken();
    }
}
