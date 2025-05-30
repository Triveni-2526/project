package com.eligibility.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.Delimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eligibility.binding.EligibilityBinding;
import com.eligibility.binding.SummaryBinding;
import com.eligibility.feign.AppRegistrationInterface;
import com.eligibility.feign.DataCollectionFeign;
import com.eligibility.feign.PlanRegistrationInterface;
import com.eligibility.service.EligibilityService;

@RestController
public class EligibilityController {
	
	
	private EligibilityService eligibilityService;
	@Autowired
	DataCollectionFeign inter;
	
	

	public EligibilityController(EligibilityService eligibilityService) {
		super();
		this.eligibilityService = eligibilityService;
	} 
	@GetMapping("/eligibility/{caseNum}")
	public ResponseEntity<EligibilityBinding> determineEligibility(@PathVariable Long caseNum) {
		EligibilityBinding eligibility = eligibilityService.determineEligibility(caseNum);
		return ResponseEntity.ok(eligibility);
	} 
	@GetMapping("/case/{caseNum}")
    public ResponseEntity<EligibilityBinding> getEligibilityByCaseNum(@PathVariable Long caseNum) {
        EligibilityBinding eligibility = eligibilityService.EligibilityByCaseNum(caseNum);
        if (eligibility != null) {
            return ResponseEntity.ok(eligibility);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	@GetMapping("/approved-citizens")
	public ResponseEntity<List<EligibilityBinding>> getApprovedCitizens() {
	    try {
	        List<EligibilityBinding> eligibleCitizens = eligibilityService.EligibleCitizens();
	        return ResponseEntity.ok(eligibleCitizens);
	    } catch (Exception e) {
	       
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}



}
