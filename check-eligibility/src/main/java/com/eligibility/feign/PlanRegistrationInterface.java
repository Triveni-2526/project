package com.eligibility.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name="admin-project",url = "http://localhost:8081")
public interface PlanRegistrationInterface {

	 
	@GetMapping("/categories")
	public ResponseEntity<Map<Integer, String>> planCategories();
	
}
