package com.car.charging.station.service.impl;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.car.charging.station.exceptionHandler.DataNotFoundException;
import com.car.charging.station.model.CarChargingSession;
import com.car.charging.station.model.CarChargingSessionResponse;
import com.car.charging.station.model.CarChargingSessionStatus;
import com.car.charging.station.model.CarChargingStore;
import com.car.charging.station.service.CarChargingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarChargingSessionServiceTest {

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";
	private static final String stationId3 = "3";
	private static final String stationId4 = "4";

	private static final String id1 = "68a8a66d-6716-4c89-ad78-78efd58c7d2f";

	@InjectMocks
	CarChargingSessionService carChargingSessionService;

	@Mock
	private CarChargingStore chargingSessionStore;

	private LocalDateTime now;

	@BeforeEach
	public void init() {
		now = LocalDateTime.now();
	}

	@Test
	@DisplayName("Test submit a charging session with given stationId")
	public void testSubmitChargingSession() {

		// when
		CarChargingSessionResponse actualResponse = carChargingSessionService.submitChargingSession(stationId1);

		// then
		assertEquals(CarChargingSessionStatus.IN_PROGRESS, actualResponse.getSessionStatus());
		assertEquals(stationId1, actualResponse.getStationId());
		assertThat(actualResponse.getId(), notNullValue());

	}

	@Test
	@DisplayName("Test stop a charging session with given id")
	public void testStopChargingSession() {

		// given
		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.fromString(id1);

		final CarChargingSession chargingSession = new CarChargingSession();

		chargingSession.setId(uuid);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);

		sessions.put(uuid, chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionResponse actualResponse = carChargingSessionService.stopChargingSession(id1);

		// then
		assertEquals(uuid, actualResponse.getId());
		assertEquals(CarChargingSessionStatus.FINISHED, actualResponse.getSessionStatus());

	}

	@Test
	@DisplayName("Test stop a charging session with non existing id.")
	public void testStopNonExistingChargingSession() {

		// given
		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();

		// then
		assertThrows(DataNotFoundException.class, () -> carChargingSessionService.stopChargingSession(id1));

	}

	@Test
	@DisplayName("Test retrieve all charging sessions")
	public void testRetrieveAllChargingSessions() {

		// given
		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		CarChargingSession chargingSession = new CarChargingSession();

		UUID uuid = UUID.randomUUID();

		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new CarChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new CarChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);

		sessions.put(uuid, chargingSession);

		chargingSession = new CarChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId4);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);

		sessions.put(uuid, chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		List<CarChargingSessionResponse> actualResponse = carChargingSessionService.retrieveAllSession();

		// then
		assertEquals(actualResponse.size(), sessions.size());

	}

	@Test
	@DisplayName("Test retrieve all with no charging sessions")
	public void testRetrieveAllEmptyChargingSessions() {

		// given
		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		List<CarChargingSessionResponse> actualResponse = carChargingSessionService.retrieveAllSession();

		// then
		assertEquals(actualResponse.size(), sessions.size());

	}

}
