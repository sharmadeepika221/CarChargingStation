package com.car.charging.station.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.car.charging.station.model.CarChargingSession;
import com.car.charging.station.model.CarChargingSessionStatus;
import com.car.charging.station.model.CarChargingSessionSummary;
import com.car.charging.station.model.CarChargingStore;
import com.car.charging.station.service.CarChargingSessionSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarChargingSessionSummaryServiceTest {

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";
	private static final String stationId3 = "3";

	@InjectMocks
	CarChargingSessionSummaryService carChargingSessionSummaryService = new CarChargingSessionSummaryServiceImpl();

	@Mock
	private CarChargingStore chargingSessionStore;

	private static final Duration TWO_MIN = Duration.ofMinutes(2L);

	private LocalDateTime now;
	private LocalDateTime twoMinBefore;

	@BeforeEach
	public void init() {
		now = LocalDateTime.now();
		twoMinBefore = LocalDateTime.now().minus(TWO_MIN);
	}

	@Test
	@DisplayName("Empty summary should be returned")
	public void testEmptySummary() {

		// when
		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(0L));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(0L));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(0L));
	}

	@Test
	@DisplayName("Only one session started in last minute then summary should return as started")
	public void testOneNewSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 0L;
		long totalCount = 1L;

		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		final CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionSummary expectedResponse = new CarChargingSessionSummary(totalCount, startedCount,
				stoppedCount);

		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions started and stopped in last minute then summary should return them as started")
	public void testMultipleNewSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 2L;
		long totalCount = 3L;

		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		UUID uuid = UUID.randomUUID();

		CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		uuid = UUID.randomUUID();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionSummary expectedResponse = new CarChargingSessionSummary(totalCount, startedCount,
				stoppedCount);

		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions stopped in last minute then summary should return them as started")
	public void testMultipleNewStoppedSessionsSummary() {

		// given
		long startedCount = 0L;
		long stoppedCount = 2L;
		long totalCount = 2L;

		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionSummary expectedResponse = new CarChargingSessionSummary(totalCount, startedCount,
				stoppedCount);

		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("More than one sessions expired in last minute then summary should not return them")
	public void testExpiredSessionsSummary() {

		// given
		long startedCount = 0L;
		long stoppedCount = 0L;
		long totalCount = 0L;

		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionSummary expectedResponse = new CarChargingSessionSummary(totalCount, startedCount,
				stoppedCount);

		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

	@Test
	@DisplayName("There are expired and recent sessions then summary should return recent sessions")
	public void testExpiredAndRecentSessionsSummary() {

		// given
		long startedCount = 1L;
		long stoppedCount = 1L;
		long totalCount = 2L;

		Map<UUID, CarChargingSession> sessions = new ConcurrentHashMap<>();

		final UUID uuid = UUID.randomUUID();

		CarChargingSession chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId1);
		chargingSession.setStartedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.IN_PROGRESS);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId2);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(twoMinBefore);
		chargingSession.setUpdatedAt(twoMinBefore);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		chargingSession = new CarChargingSession();
		chargingSession.setId(uuid);
		chargingSession.setStationId(stationId3);
		chargingSession.setStartedAt(twoMinBefore);
		chargingSession.setStoppedAt(now);
		chargingSession.setUpdatedAt(now);
		chargingSession.setSessionStatus(CarChargingSessionStatus.FINISHED);
		sessions.put(UUID.randomUUID(), chargingSession);

		// when
		doReturn(sessions).when(chargingSessionStore).getSessions();
		CarChargingSessionSummary expectedResponse = new CarChargingSessionSummary(totalCount, startedCount,
				stoppedCount);

		CarChargingSessionSummary chargingSessionsSummaryResponse = carChargingSessionSummaryService
				.retrieveChargingSessionSummary();

		// then
		assertThat(chargingSessionsSummaryResponse, is(notNullValue()));
		assertThat(chargingSessionsSummaryResponse.getStartedCount(), equalTo(expectedResponse.getStartedCount()));
		assertThat(chargingSessionsSummaryResponse.getStoppedCount(), equalTo(expectedResponse.getStoppedCount()));
		assertThat(chargingSessionsSummaryResponse.getTotalCount(), equalTo(expectedResponse.getTotalCount()));
	}

}
