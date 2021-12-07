package com.example.application.data.entity;

import com.example.application.data.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.fusion.Nonnull;
import java.time.Instant;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data

@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    @Nonnull
    private Integer id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @JsonIgnore
    private String hashedPassword;

    @NotBlank
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Lob
    private String profilePictureUrl;

    private Instant createdAt;

    public User(@Nonnull Integer id, @NotBlank String username,
        @NotBlank @Email String email, String hashedPassword,
        @NotBlank String phoneNumber, Set<Role> roles, String profilePictureUrl, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
        this.profilePictureUrl = profilePictureUrl;
        this.createdAt = createdAt;
    }
}
