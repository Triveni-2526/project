package com.correspondence.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligibilityBinding {
	private Long caseNum;
	private String holderName;
	private Long holderSsn;
	private String planName;
	private Boolean planStatus= false;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmt;
	private String denialReason;
	

}
