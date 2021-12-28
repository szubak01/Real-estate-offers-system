package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

  @OneToOne(mappedBy = "location")
  private Offer offer;
}
