package com.car.charging.station.service;

import java.util.List;

import com.car.charging.station.model.CarChargingSessionResponse;

public interface CarChargingSessionService {

	List<CarChargingSessionResponse> retrieveAllSession();
	CarChargingSessionResponse stopChargingSession(String id);
	CarChargingSessionResponse submitChargingSession(String stationId);

}
