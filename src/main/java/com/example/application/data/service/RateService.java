package com.example.application.data.service;

import com.example.application.data.entity.Rate;
import com.example.application.data.entity.User;
import com.example.application.data.repository.RateRepository;
import java.util.List;
import java.util.stream.Collectors;
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

//  public List<Rate> getUserRates() {
//    return rateRepository.findAll()
//        .stream()
//        .filter(rate -> !rate.isRenterRate())
//        .collect(Collectors.toList());
//  }

  public List<Rate> getUserRates(User user) {
    return rateRepository.findAll()
        .stream()
        .filter(rate -> rate.getPersonRated().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public void delete(Integer id) {
    rateRepository.deleteById(id);
  }

  public List<Rate> findAllRates(String value) {
    if (value == null || value.isEmpty()) {
      return rateRepository.findAll();
    } else {
      return rateRepository.search(value);
    }
  }

//
//  public Collection<? extends Rate> getStudentRates() {
//    return rateRepository.findAll()
//        .stream()
//        .filter(Rate::isRenterRate)
//        .collect(Collectors.toList());
//  }

//  public List<Rate> getSpecificStudentRates(User student) {
//    return rateRepository.findAll()
//        .stream()
//        .filter(rate -> rate.getPersonRated().equals(student.getId()))
//        .collect(Collectors.toList());
//  }
}
