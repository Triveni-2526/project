package com.correspondence.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.correspondence.binding.CaseResponse;



@FeignClient(name = "data-collection",url = "http://localhost:8085")
public interface DataCollectionFeign {

	@GetMapping("/case/{caseNum}")
	public CaseResponse getCaseEntity(@PathVariable Long caseNum);
}
