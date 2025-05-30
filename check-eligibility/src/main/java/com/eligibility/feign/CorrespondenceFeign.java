package com.eligibility.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eligibility.binding.CoTriggerResponse;


@FeignClient(name = "correspondence",url = "http://localhost:8087")
public interface CorrespondenceFeign {

	 @PostMapping("/api/triggers")
	    CoTriggerResponse saveTrigger(@RequestBody CoTriggerResponse triggerResponse);
	}
