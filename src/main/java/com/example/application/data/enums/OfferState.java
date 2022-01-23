package com.example.application.data.enums;

import lombok.Getter;

@Getter
public enum OfferState {

  OPEN("Open"),
  CLOSED("Closed"),
  RENTED_OUT("Rented out"),
  RENT_FINISHED("Rent finished");

  private final String offerState;

  OfferState(String offerState){
    this.offerState = offerState;
  }

}