package com.car.charging.station.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.car.charging.station.model.CarChargingSessionRequest;
import com.car.charging.station.model.CarChargingSessionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ChargingSessionIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String stationId1 = "1";
	private static final String stationId2 = "2";

	@Test
	@DisplayName("Charging sessions started and should be returned from retrieveAll")
	void testRetrieveAllSubmittedChargingSession() throws Exception {
		CarChargingSessionRequest chargingSessionRequest1 = new CarChargingSessionRequest();
		chargingSessionRequest1.setStationId(stationId1);

		mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(asJsonString(chargingSessionRequest1)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.stationId", equalTo(stationId1)))
				.andExpect(jsonPath("$.sessionStatus", equalTo(CarChargingSessionStatus.IN_PROGRESS.toString())));

		CarChargingSessionRequest chargingSessionRequest2 = new CarChargingSessionRequest();
		chargingSessionRequest2.setStationId(stationId2);

		mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(chargingSessionRequest2)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.stationId", equalTo(stationId2)))
				.andExpect(jsonPath("$.sessionStatus", equalTo(CarChargingSessionStatus.IN_PROGRESS.toString())));

		mockMvc.perform(get("/chargingSessions"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].sessionStatus", equalTo(CarChargingSessionStatus.IN_PROGRESS.toString())))
				.andExpect(jsonPath("$[1].sessionStatus", equalTo(CarChargingSessionStatus.IN_PROGRESS.toString())));

	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
