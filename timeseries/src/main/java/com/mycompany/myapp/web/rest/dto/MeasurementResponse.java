package com.mycompany.myapp.web.rest.dto;

public class MeasurementResponse {
    
    private boolean success;
    
    public MeasurementResponse(boolean success) {
        this.success = success;
    }
    
    public boolean getSuccess(){
        return success;
    }
}
