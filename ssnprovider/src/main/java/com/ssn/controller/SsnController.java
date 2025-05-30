package com.ssn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ssn.service.SsnService;

@RestController
public class SsnController {

	private SsnService service;
	
	
	public SsnController(SsnService service) {
		super();
		this.service = service;
	}


	@GetMapping("getState/{ssn}")
	public ResponseEntity<String> stateSsnBelong(@PathVariable String ssn) {
		String stateFromSsn = service.getStateFromSsn(ssn);
		return ResponseEntity.ok(stateFromSsn);
		// TODO Auto-generated constructor stub
	}
}
