package com.mycompany.myapp.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.service.MeasurementService;
import com.mycompany.myapp.web.rest.dto.MeasurementResponse;

@RestController
@RequestMapping("/api")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    @Inject
    private MeasurementService measurementService;

    @RequestMapping(value = "/measurement", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MeasurementResponse save(@Valid @RequestBody Measurement measurement) {
        log.info("Request to save Measurement : {}", measurement);
        try {
            measurementService.save(measurement);
            return new MeasurementResponse(true);
        } catch (Exception e) {
            log.error("Error to save Measurement: ", e);
            return new MeasurementResponse(false);
        }
    }

}
