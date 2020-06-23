package com.car.charging.station.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Logger;
import com.car.charging.station.exceptionHandler.DataNotFoundException;
import com.car.charging.station.model.CarChargingSession;
import com.car.charging.station.model.CarChargingSessionResponse;
import com.car.charging.station.model.CarChargingSessionStatus;
import com.car.charging.station.model.CarChargingStore;
import com.car.charging.station.service.CarChargingSessionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarChargingSessionServiceImpl implements CarChargingSessionService {

	@Autowired
	private CarChargingStore sessionStore;
	Logger log = (Logger) LoggerFactory.getLogger(CarChargingSessionServiceImpl.class);

	@Override
	public List<CarChargingSessionResponse> retrieveAllSession() {
		return sessionStore.getSessions().values().stream().map(CarChargingSessionResponse::of)
				.collect(Collectors.toList());
	}

	@Override
	public CarChargingSessionResponse stopChargingSession(String id) {
		final UUID uuid = UUID.fromString(id);
		final CarChargingSession chargingSession = sessionStore.getSessions().values().stream()
				.filter(s -> (uuid.equals(s.getId()) && s.getSessionStatus() == CarChargingSessionStatus.IN_PROGRESS)).findAny()
				.orElseThrow(() -> new DataNotFoundException("There is no in progress session with id: " + id));

		final LocalDateTime stoppedAt = LocalDateTime.now();
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		chargingSession.setUpdatedAt(stoppedAt);
		chargingSession.setStoppedAt(stoppedAt);
		log.info("Session stopped with id " + id);
		return CarChargingSessionResponse.of(chargingSession);
	}

	@Override
	public CarChargingSessionResponse submitChargingSession(String stationId) {
		final LocalDateTime startedAt = LocalDateTime.now();
		final UUID uuid = UUID.randomUUID();

		final CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId);
		chargingSession.setStartedAt(startedAt);
		chargingSession.setUpdatedAt(startedAt);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		sessionStore.getSessions().put(uuid, chargingSession);

		log.info("Session started in station " + stationId);

		return CarChargingSessionResponse.of(chargingSession);
	}
}
