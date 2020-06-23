package com.car.charging.station.controller;

import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.car.charging.station.model.CarChargingSessionRequest;
import com.car.charging.station.model.CarChargingSessionResponse;
import com.car.charging.station.model.CarChargingSessionStatus;
import com.car.charging.station.model.CarChargingSessionSummary;
import com.car.charging.station.service.CarChargingSessionService;
import com.car.charging.station.service.CarChargingSessionSummaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CarChargingSessionControllerTest {

	private static final String stationId1 = "abc-123";
	private static final String stationId2 = "xyz-123";
	private static final String stationIdBlank = "";

	@MockBean
	private CarChargingSessionService chargingSessionService;

	@MockBean
	private CarChargingSessionSummaryService chargingSessionSummaryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("POST /chargingSessions - Success")
	void testSubmitChargingSession() throws Exception {

		CarChargingSessionRequest chargingSessionRequest = new CarChargingSessionRequest();
		chargingSessionRequest.setStationId(stationId1);
		CarChargingSessionResponse chargingSession = new CarChargingSessionResponse(UUID.randomUUID(), stationId1, now(),
				CarChargingSessionStatus.IN_PROGRESS);
		doReturn(chargingSession).when(chargingSessionService).submitChargingSession(stationId1);
		mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(chargingSessionRequest)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id", equalTo(chargingSession.getId().toString())))
				.andExpect(jsonPath("$.stationId", equalTo(chargingSession.getStationId())))
				.andExpect(jsonPath("$.updatedAt",
						equalTo(chargingSession.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
				.andExpect(jsonPath("$.sessionStatus", equalTo(chargingSession.getSessionStatus().toString())));

	}

	@Test
	@DisplayName("PUT /chargingSessions - Success")
	void testStopChargingSession() throws Exception {
		String id = stationId1;
		CarChargingSessionResponse chargingSession = new CarChargingSessionResponse(UUID.randomUUID(), id, now(),
				CarChargingSessionStatus.FINISHED);
		doReturn(chargingSession).when(chargingSessionService).stopChargingSession(id);
		mockMvc.perform(put("/chargingSessions/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id", equalTo(chargingSession.getId().toString())))
				.andExpect(jsonPath("$.stationId", equalTo(chargingSession.getStationId())))
				.andExpect(jsonPath("$.updatedAt",
						equalTo(chargingSession.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
				.andExpect(jsonPath("$.sessionStatus", equalTo(chargingSession.getSessionStatus().toString())));

	}

	@Test
	@DisplayName("GET /chargingSessions - Success")
	void testGetChargingSessions() throws Exception {
		List<CarChargingSessionResponse> chargingSessions = new ArrayList<>();

		chargingSessions.add(new CarChargingSessionResponse(UUID.randomUUID(), stationId1, now(), CarChargingSessionStatus.IN_PROGRESS));
		chargingSessions.add(new CarChargingSessionResponse(UUID.randomUUID(), stationId2, now(), CarChargingSessionStatus.IN_PROGRESS));

		doReturn(chargingSessions).when(chargingSessionService).retrieveAllSession();

		mockMvc.perform(get("/chargingSessions"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", equalTo(chargingSessions.get(0).getId().toString())))
				.andExpect(jsonPath("$[0].stationId", equalTo(chargingSessions.get(0).getStationId())))
				.andExpect(jsonPath("$[0].updatedAt",
						equalTo(chargingSessions.get(0).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
				.andExpect(jsonPath("$[0].sessionStatus", equalTo(chargingSessions.get(0).getSessionStatus().toString())))
				.andExpect(jsonPath("$[1].id", equalTo(chargingSessions.get(1).getId().toString())))
				.andExpect(jsonPath("$[1].stationId", equalTo(chargingSessions.get(1).getStationId())))
				.andExpect(jsonPath("$[1].updatedAt",
						equalTo(chargingSessions.get(1).getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
				.andExpect(jsonPath("$[1].sessionStatus", equalTo(chargingSessions.get(1).getSessionStatus().toString())));

	}

	@Test
	@DisplayName("GET /chargingSessions/summary - Success")
	void testGetChargingSessionsSummary() throws Exception {

		CarChargingSessionSummary chargingSessionsSummary = new CarChargingSessionSummary(3L, 1L, 2L);
		doReturn(chargingSessionsSummary).when(chargingSessionSummaryService).retrieveChargingSessionSummary();
		mockMvc.perform(get("/chargingSessions/summary"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.totalCount", equalTo((int) chargingSessionsSummary.getTotalCount())))
				.andExpect(jsonPath("$.startedCount", equalTo((int) chargingSessionsSummary.getStartedCount())))
				.andExpect(jsonPath("$.stoppedCount", equalTo((int) chargingSessionsSummary.getStoppedCount())));

	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
