package com.report.binding;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EligibilityResponse {


	private String name;

	private Long mobile;

	private String email;

	private String gender;

	private Long ssn;

	private String planName;

	private String planStatus;

	private LocalDate planStartDate;

	private LocalDate planEndDate;
}
