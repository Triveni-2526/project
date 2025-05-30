package com.eligibility.binding;

import java.util.List;

import lombok.Data;

@Data
public class SummaryBinding {

	private Integer appId;
	
	private IncomeBinding income;
	
	private EducationBinding education;
	
	private List<ChildrenBinding> children;
	
	private String planName;
}
