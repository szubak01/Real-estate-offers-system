package com.example.application.data.service;

import com.example.application.data.entity.Rate;
import com.example.application.data.repository.RateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RateService {

  private final RateRepository rateRepository;

  public void save(Rate rate){
    rateRepository.save(rate);
    log.info("Rate saved to database");
  }

}
