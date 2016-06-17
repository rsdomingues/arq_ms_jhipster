package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * An measurment value to be stored in elasticserach
 * 
 * @author rodrigosd
 */

@Document(indexName = "measurement",type = "measurement" , shards = 5, replicas = 0)
public class Measurement implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    
    private LocalDateTime timestamp;
    
    private Long equipmentId;
    
    private String latitude;
    
    private String longitude;
    
    private Double value;
    
    private String unit;
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }
    
    public long getEquipmentId() {
        return equipmentId;
    } 
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public String getLongitude() {
        return longitude;
    }
  
    public void setValue(Double value) {
        this.value = value;
    }
    
    public Double getValue() {
        return value;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getUnit() {
        return unit;
    }
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "{id:" + id + ", timestamp:" + timestamp + ", equipmentId:" + equipmentId
                + ", latitude:" + latitude + ", longitude:" + longitude + ", value:" + value + ", unit:"
                + unit + "}";
    }
}
