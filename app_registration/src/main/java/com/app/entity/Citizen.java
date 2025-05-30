package com.app.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CITIZEN")
@Data
public class Citizen {
	
	@Id
	@Column(name = "APP_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer appId;
	
	@Column(name = "FULL_NAME")
	private String fullName;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "MOBILE")
	private Long mobile;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "STATE_NAME")
	private String stateName;
	
	@Column(name = "DOB")
	private LocalDate dob;
	
	@Column(name = "SSN")
	private Long ssn;
	
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_DATE", updatable = false)
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(name = "UPDATED_DATE", insertable = false)
	@UpdateTimestamp
	private LocalDate updatedDate;

}
