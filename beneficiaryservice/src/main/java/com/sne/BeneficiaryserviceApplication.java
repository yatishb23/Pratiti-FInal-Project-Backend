package com.sne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BeneficiaryserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeneficiaryserviceApplication.class, args);
	}

}
