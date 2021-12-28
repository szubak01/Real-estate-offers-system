package com.example.application.data.service;

import com.example.application.data.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {

  private final LocationRepository locationRepository;
}
