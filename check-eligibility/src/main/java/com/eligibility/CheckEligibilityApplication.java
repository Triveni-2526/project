package com.eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eligibility.feign")
public class CheckEligibilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckEligibilityApplication.class, args);
	}

}
