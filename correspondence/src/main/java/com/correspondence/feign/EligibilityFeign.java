package com.correspondence.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.correspondence.binding.EligibilityBinding;




@FeignClient(name = "check-eligibility",url = "http://localhost:8086")
public interface EligibilityFeign {

	 @GetMapping("/case/{caseNum}")
	    ResponseEntity<EligibilityBinding> getEligibilityByCaseNum(@PathVariable Long caseNum);
	}