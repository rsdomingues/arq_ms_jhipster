package com.mycompany.myapp.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.repository.MeasurementRepository;

@Service
public class MeasurementService {

    private final Logger log = LoggerFactory.getLogger(MeasurementService.class);

    @Inject
    private MeasurementRepository measurementRepository;

    /**
     * Save a Measurement
     * 
     * @param measurement the entity to save
     * @return the persisted entity
     */
    public Measurement save(Measurement measurement) {
        log.debug("Request to save Measurement : {}", measurement);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setId(null);
        return measurementRepository.save(measurement);
    }

}
