package com.correspondence.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.correspondence.binding.CitizenData;

@FeignClient(name="app-registration",url = "http://localhost:8084")
public interface AppRegistrationFeign {

	@GetMapping("/citizen/{appId}")
	public CitizenData findCitizenById(@PathVariable Integer appId);
}
