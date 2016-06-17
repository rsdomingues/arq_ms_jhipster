package com.mycompany.myapp.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.mycompany.myapp.domain.Measurement;

public interface MeasurementRepository extends ElasticsearchCrudRepository<Measurement, Long>{

}
