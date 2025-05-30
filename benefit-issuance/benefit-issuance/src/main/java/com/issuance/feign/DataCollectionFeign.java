package com.issuance.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.issuance.binding.CaseResponse;




@FeignClient(name = "data-collection-service", url = "http://localhost:8085")
public interface DataCollectionFeign {

    @GetMapping("/case/{caseId}")
    CaseResponse getCaseEntity(@PathVariable("caseId") Long caseId);
}

