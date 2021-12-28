package com.example.application.data.entity;

import com.example.application.data.enums.OfferType;
import java.time.Instant;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private OfferType offerTypeSelect;
  @NotBlank(message = "Cannot be empty")
  private String offerTitle;
  @NotNull(message = "Cannot be empty")
  private Double pricePerMonth;
  @NotNull(message = "Cannot be empty")
  private Double rent;
  @NotNull(message = "Cannot be empty")
  private Double deposit;
  @NotNull(message = "Cannot be empty")
  private Double livingArea;
  @NotNull(message = "Cannot be empty")
  private Double numberOfRooms;
  private String typeOfRoom;
  @NotBlank(message = "Cannot be empty")
  private String description;

  @ElementCollection
  private Set<String> offerImages;

  @OneToOne
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private Instant createdAt;
}
