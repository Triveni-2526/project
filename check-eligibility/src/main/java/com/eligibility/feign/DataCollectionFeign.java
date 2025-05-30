package com.eligibility.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eligibility.binding.SummaryBinding;


@FeignClient(name = "data-collection",url = "http://localhost:8085")
public interface DataCollectionFeign {

	@GetMapping("/summary/{caseNum}")
	public ResponseEntity<SummaryBinding> getSummary(@PathVariable Long caseNum);
}
