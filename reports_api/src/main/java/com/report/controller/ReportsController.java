package com.report.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.report.binding.EligibilityResponse;
import com.report.binding.SearchRequest;
import com.report.entity.EligibilityDtls;
import com.report.entity.SearchResponse;
import com.report.service.ReportsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ReportsController {

	
	private ReportsService reportsService;
	public ReportsController(ReportsService reportsService) {
		super();
		this.reportsService = reportsService;
	}
	
	@PostMapping("/eligibilityDtls")
	public ResponseEntity<List<EligibilityDtls>> savePlans(@RequestBody List<EligibilityResponse> plans){
		List<EligibilityDtls> savePlans = reportsService.savePlans(plans);
		return new ResponseEntity<>(savePlans,HttpStatus.CREATED);
	}
	@GetMapping("/plan-status")
	public ResponseEntity<List<String>> showPlanStatus(){
		List<String> planStatus = reportsService.getUniquePlanStatus();
		return new ResponseEntity<>(planStatus,HttpStatus.OK);
	}
	@GetMapping("/plan-names")
	public ResponseEntity<List<String>> showPlanNames(){
		List<String> planStatus = reportsService.getUniquePlanNames();
		return new ResponseEntity<>(planStatus,HttpStatus.OK);
	}
	@PostMapping("/search") 
	public ResponseEntity<List<SearchResponse>> search(@RequestBody(required = false) SearchRequest request){
		List<SearchResponse> searchResponse = reportsService.search(request);
		return new ResponseEntity<>(searchResponse,HttpStatus.OK);
	}
	@PostMapping("/search2") 
	public ResponseEntity<List<SearchResponse>> search2(@RequestBody SearchRequest request){
		List<SearchResponse> searchResponse = reportsService.search2(request);
		return new ResponseEntity<>(searchResponse,HttpStatus.OK);
	}
	
	@GetMapping("/excel")
	public void excelReport(@RequestBody SearchRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.xlsx";
		response.setHeader(headerKey, headerValue);
		
		reportsService.generateExcel(response, request);
	}
	@GetMapping("/pdf")
	public void pdfReport(@RequestBody SearchRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=data.pdf ";
		response.setHeader(headerKey, headerValue);
		
		reportsService.generatePdf(response, request);
	}
}
