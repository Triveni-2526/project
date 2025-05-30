package com.correspondence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CO_TRIGGERS")
public class CoTrigger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trigId;
	
	private Long caseNum;
	
	@Lob
	private byte[] coPdf;
	
	private String trgStatus;
}
