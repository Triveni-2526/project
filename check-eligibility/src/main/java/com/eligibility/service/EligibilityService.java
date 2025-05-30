package com.eligibility.service;

import java.util.List;

import com.eligibility.binding.EligibilityBinding;

public interface EligibilityService {
	
	public EligibilityBinding determineEligibility(Long caseNum);
	
	public EligibilityBinding EligibilityByCaseNum(Long caseNum);
	
	public List<EligibilityBinding> EligibleCitizens();
}
