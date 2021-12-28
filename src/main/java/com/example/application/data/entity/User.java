package com.example.application.data.entity;

import com.example.application.data.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.fusion.Nonnull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.annotation.RegEx;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Nonnull
  private Integer id;

  @NotBlank(message = "Provide username.")
  private String username;

  @NotBlank(message = "Provide email.")
  @Email(message = "Email must be properly formatted.\n  e.g. example@gmail.com")
  private String email;

  @JsonIgnore
  @Size(min = 8, max = 64, message = "Password must be 8-64 char long")
  private String password;

  @Pattern(regexp = "^\\d{9}$", message = "Provide 9 digits number.")
  @NotBlank(message = "Provide phone number.")
  private String phoneNumber;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @Lob
  private byte[] profilePictureUrl;

  private Instant createdAt;

  //Additional info
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String city;

  @OneToMany(mappedBy = "user")
  private List<Offer> offers;
}
