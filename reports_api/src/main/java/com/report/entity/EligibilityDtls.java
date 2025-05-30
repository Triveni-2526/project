package com.report.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
@Table(name = "ELIGIBILITY_DTLS")
public class EligibilityDtls {

	@Id
	@GeneratedValue
	@Column(name = "ELIG_ID")
	private Integer eligibilityId;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "MOBILE")
	private Long mobile;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "SSN")
	private Long ssn;
	
	@Column(name = "PLAN_NAME")
	private String planName;
	
	@Column(name = "PLAN_STATUS")
	private String planStatus;
	
	@Column(name="PLAN_START_DATE")
	private LocalDate planStartDate;
	
	@Column(name = "PLAN_END_DATE")
	private LocalDate planEndDate;
	
	@CreationTimestamp
	@Column(name = "CREATED_DATE", updatable = false)
	private LocalDate createdDate;
	
	@UpdateTimestamp
	@Column(name="UPDATED_DATE", insertable = false)
	private LocalDate updatedDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
}
