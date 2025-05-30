package com.report.service;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.report.binding.EligibilityResponse;
import com.report.binding.SearchRequest;
import com.report.entity.EligibilityDtls;
import com.report.entity.SearchResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportsService {
	
	public List<EligibilityDtls> savePlans(List<EligibilityResponse> plans);
	
	public List<String> getUniquePlanNames();
	
	public List<String> getUniquePlanStatus();
	
	public List<SearchResponse> search(SearchRequest request);
	
	public List<SearchResponse> search2(SearchRequest request);
	
	public void generateExcel(HttpServletResponse respone,SearchRequest request) throws IOException;
	
	public void generatePdf(HttpServletResponse respone,SearchRequest request) throws DocumentException, IOException;

	

}
