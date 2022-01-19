package com.example.application.data.entity;

import com.example.application.data.enums.OfferType;
import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

  private Instant createdAt;
  private Instant updatedAt;

  @OneToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(fetch = FetchType.LAZY)
  private List<OfferImage> images;

}
