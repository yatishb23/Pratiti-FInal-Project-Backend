package com.rohan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EbankingsubsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingsubsystemApplication.class, args);
		
	}
}
