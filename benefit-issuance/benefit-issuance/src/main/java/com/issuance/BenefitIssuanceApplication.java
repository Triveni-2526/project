package com.issuance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BenefitIssuanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BenefitIssuanceApplication.class, args);
	}

}
