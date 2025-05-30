package com.eligibility.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eligibility.binding.CitizenData;


@FeignClient(name="app-registration",url = "http://localhost:8084")
public interface AppRegistrationInterface {

	@GetMapping("/citizen/{appId}")
	public CitizenData findCitizenById(@PathVariable Integer appId);
	
}
