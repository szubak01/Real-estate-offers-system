package com.example.application.data.enums;

import lombok.Getter;

@Getter
public enum OfferType {

  Apartment("Apartment"),
  Room("Room");

  private final String offerType;

  OfferType(String offerType){
    this.offerType = offerType;
  }

}
