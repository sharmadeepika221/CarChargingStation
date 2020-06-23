package com.car.charging.station.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CarChargingSession {

    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private LocalDateTime updatedAt;
    private CarChargingSessionStatus sessionStatus;

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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
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
