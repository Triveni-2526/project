package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.binding.CitizenData;
import com.app.service.CitizenServiceImpl;

@RestController
public class CitizenController {
	
	CitizenServiceImpl impl;

	public CitizenController(CitizenServiceImpl impl) {
		super();
		this.impl = impl;
	}

	@PostMapping("/app")
	public ResponseEntity<String> createApplication(@RequestBody CitizenData citizenData){
		Integer applicationId = impl.createApplication(citizenData);
		
		if (applicationId>0) {
			return ResponseEntity.ok("Registration Successful for Id: "+applicationId);
		}else {
			return ResponseEntity.badRequest().body("Invalid SSN");
		}
	}
	@GetMapping("/app/{id}")
	public ResponseEntity<Boolean> findById(@PathVariable Integer id){
		Boolean isappIdPresent = impl.findById(id);
		
		if (isappIdPresent) {
			return ResponseEntity.ok(true);
		}else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	@GetMapping("/citizen/{appId}")
	public ResponseEntity<CitizenData> findCitizenById(@PathVariable Integer appId) {
		CitizenData citizenById = impl.findCitizenById(appId);
		return ResponseEntity.ok(citizenById);
	}

}
