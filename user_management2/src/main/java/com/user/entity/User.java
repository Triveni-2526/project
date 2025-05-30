package com.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@Column(name = "FULL_NAME")
	private String fullName;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "MOBILE")
	private Long mobile;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "DATE_OF_BIRTH")
	private LocalDate dateOfBirth;
	
	@Column(name = "SSN")
	private Long ssn;
	
	@Column(name = "ACC_STATUS")
	private String accStatus;
	
//	@Column(name="TEMP_PASSWORD")
//	private String tempPassword;
	
	@Column(name="PASSWORD")
	private String Password;
	
	@Column(name="CREATED_DATE", updatable = false)
	private LocalDate createdDate;
	
	@Column(name="UPDATED_DATE", insertable = false)
	private LocalDate updatedDate;
	
	@Column(name = "CREATED_BY")
	private LocalDate createdBy;
	
	@Column(name = "UPDATED_BY")
	private LocalDate updatedBy;
}
