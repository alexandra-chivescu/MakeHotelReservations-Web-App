package com.HotelAlexa.rezervaCamera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RezervaCameraApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezervaCameraApplication.class, args);
	}

}
