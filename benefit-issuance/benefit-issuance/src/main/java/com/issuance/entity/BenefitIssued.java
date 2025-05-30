package com.issuance.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "BENEFIT_ISSUED")
public class BenefitIssued {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer benefitId;
	private Long caseNum;
	private String holderName;
	private String email;
	private Long mobile;
	private Long holderSsn;
	private String planName;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benefitAmt;
}

