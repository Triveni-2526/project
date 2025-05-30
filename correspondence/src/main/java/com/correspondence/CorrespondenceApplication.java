package com.correspondence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CorrespondenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorrespondenceApplication.class, args);
	}

}
