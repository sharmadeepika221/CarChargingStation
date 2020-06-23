package com.car.charging.station.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarChargingSessionResponse {

    private UUID id;
    private String stationId;
    private LocalDateTime updatedAt;
    private CarChargingSessionStatus sessionStatus;

    public CarChargingSessionResponse(UUID id, String stationId, LocalDateTime updatedAt, CarChargingSessionStatus sessionStatus) {
        this.id = id;
        this.stationId = stationId;
        this.updatedAt = updatedAt;
        this.sessionStatus = sessionStatus;
    }

    public static CarChargingSessionResponse of(final CarChargingSession chargingSession) {
        return new CarChargingSessionResponse(chargingSession.getId(),
                chargingSession.getStationId(),
                chargingSession.getUpdatedAt(),
                chargingSession.getSessionStatus());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CarChargingSessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(CarChargingSessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
