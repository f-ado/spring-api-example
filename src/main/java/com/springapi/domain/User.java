package com.springapi.domain;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
public class User extends DateAudit {
    @Id
    private UUID id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 40)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 150)
    @Column(name = "password")
    private String password;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @NotBlank
    @Size(max = 150)
    @Column(name = "confirmation_token")
    private String confirmationToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
