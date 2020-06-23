package com.car.charging.station.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import ch.qos.logback.classic.Logger;
import com.car.charging.station.model.CarChargingSessionStatus;
import com.car.charging.station.model.CarChargingSessionSummary;
import com.car.charging.station.model.CarChargingStore;
import com.car.charging.station.service.CarChargingSessionSummaryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarChargingSessionSummaryServiceImpl implements CarChargingSessionSummaryService {
	@Autowired
	private CarChargingStore sessionStore;

	private static final Duration MIN = Duration.ofMinutes(1L);
	Logger log = (Logger) LoggerFactory.getLogger(CarChargingSessionSummaryServiceImpl.class);

	private long countStopped(LocalDateTime oneMinBefore) {
		return sessionStore.getSessions().values().stream()
				.filter(s -> (s.getSessionStatus() == CarChargingSessionStatus.FINISHED && s.getStoppedAt().isBefore(oneMinBefore))).count();
	}

	private long countStarted(LocalDateTime oneMinBefore) {
		return sessionStore.getSessions().values().stream()
				.filter(s -> (s.getSessionStatus() == CarChargingSessionStatus.IN_PROGRESS &&s.getStartedAt().isBefore(oneMinBefore))).count();
	}

	/**
	 * Retrieves charging sessions summary for the last minute. Time complexity is O(n).
	 *
	 * @return CarChargingSessionsSummary
	 */
	@Override
	public CarChargingSessionSummary retrieveChargingSessionSummary() {
		LocalDateTime oneMinBefore = LocalDateTime.now().minus(MIN);

		long startedCount = countStarted(oneMinBefore);
		long stoppedCount = countStopped(oneMinBefore);
		log.info("Count of Charging session which are started: " + startedCount + " stopped: " + stoppedCount + ".");

		return new CarChargingSessionSummary(startedCount + stoppedCount, startedCount, stoppedCount);
	}
}
