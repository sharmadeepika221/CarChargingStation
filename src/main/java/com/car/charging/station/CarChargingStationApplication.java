package com.car.charging.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value={"classpath:my-project.properties"})
public class CarChargingStationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarChargingStationApplication.class, args);
	}

}
