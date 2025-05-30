package com.issuance.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;

import com.issuance.binding.EligibilityBinding;

@FeignClient(name = "check-eligibility", url = "http://localhost:8086")
public interface EligibilityFeign {

	@GetMapping("/approved-citizens")
	public List<EligibilityBinding> EligibleCitizens();
}