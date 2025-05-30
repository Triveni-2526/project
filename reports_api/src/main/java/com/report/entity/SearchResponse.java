package com.report.entity;

import lombok.Data;

@Data
public class SearchResponse  {
	private String name;
	private Long mobile;
	private String email;
	private String gender;
	private Long ssn;

}
