package com.car.charging.station.config;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.car.charging.station.model.CarChargingSession;
import com.car.charging.station.model.CarChargingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChargingSessionStoreConfig {
	@Bean
	public CarChargingStore chargingSessionStore() {
		return new CarChargingStore(new ConcurrentHashMap<UUID, CarChargingSession>());
	}

}
