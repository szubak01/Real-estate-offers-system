package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Cannot be empty")
  private String city;
  @NotBlank(message = "Cannot be empty")
  private String voivodeship;
  @NotBlank(message = "Cannot be empty")
  private String streetNumber;
  @NotBlank(message = "Cannot be empty")
  private String postalCode;

  @OneToOne(mappedBy = "location", orphanRemoval = true, fetch = FetchType.LAZY)
  private Offer offer;
}
