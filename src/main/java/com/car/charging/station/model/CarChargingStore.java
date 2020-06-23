package com.car.charging.station.model;

import java.util.Map;
import java.util.UUID;

public class CarChargingStore {

    private final Map<UUID, CarChargingSession> sessions;
    public CarChargingStore(Map<UUID, CarChargingSession> sessions) {
        this.sessions = sessions;
    }

    public Map<UUID, CarChargingSession> getSessions() {
        return sessions;
    }



}
