package com.car.charging.station.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;

public class CarChargingSessionRequest {

    private String stationId;
    @NotBlank
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
