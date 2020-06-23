package com.car.charging.station.controller;

import static org.springframework.http.ResponseEntity.ok;
import javax.validation.Valid;
import com.car.charging.station.model.CarChargingSessionRequest;
import com.car.charging.station.model.CarChargingSessionResponse;
import com.car.charging.station.model.CarChargingSessionSummary;
import com.car.charging.station.service.CarChargingSessionService;
import com.car.charging.station.service.CarChargingSessionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarChargingSessionController {

	@Autowired
	private CarChargingSessionService chargingSessionService;
	@Autowired
	private CarChargingSessionSummaryService chargingSessionSummaryService;

	@PostMapping("/chargingSessions")
	public ResponseEntity<CarChargingSessionResponse> createChargingSession(
			@RequestBody @Valid CarChargingSessionRequest request) {
		return ok().body(chargingSessionService.submitChargingSession(request.getStationId()));
	}

	@PutMapping("/chargingSessions/{id}")
	public ResponseEntity<CarChargingSessionResponse> stopChargingSession(@PathVariable String id) {
		return ok().body(chargingSessionService.stopChargingSession(id));
	}

	@GetMapping("/chargingSessions")
	public ResponseEntity<List<CarChargingSessionResponse>> getSessions() {
		return ok().body(chargingSessionService.retrieveAllSession());
	}

	@GetMapping("/chargingSessions/summary")
	public ResponseEntity<CarChargingSessionSummary> getSessionsSummary() {
		return ok().body(chargingSessionSummaryService.retrieveChargingSessionSummary());
	}

}
