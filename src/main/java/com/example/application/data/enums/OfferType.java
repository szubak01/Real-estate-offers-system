package com.example.application.data.enums;

import lombok.Getter;

@Getter
public enum OfferType {

  Apartment("apartment"),
  Room("room");

  private final String offerType;

  OfferType(String offerType){
    this.offerType = offerType;
  }

}
