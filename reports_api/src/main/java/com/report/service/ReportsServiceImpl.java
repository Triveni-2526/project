package com.report.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.report.binding.EligibilityResponse;
import com.report.binding.SearchRequest;
import com.report.entity.EligibilityDtls;
import com.report.entity.SearchResponse;
import com.report.repo.EligibilityDtlsRepo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportsServiceImpl implements ReportsService {

	private EligibilityDtlsRepo repo;

	public ReportsServiceImpl(EligibilityDtlsRepo repo) {
		super();
		this.repo = repo;
	}

	@Override
	public List<EligibilityDtls> savePlans(List<EligibilityResponse> responseList) {
		List<EligibilityDtls> outList = new ArrayList<>();
//		System.out.println("received fron controler response" + responseList);
		for (EligibilityResponse eligibilityResponse : responseList) {
			EligibilityDtls eligibilityDtls = new EligibilityDtls();

			BeanUtils.copyProperties(eligibilityResponse, eligibilityDtls);
			outList.add(eligibilityDtls);
		}
//		System.out.println("after converting response" + outList);
		List<EligibilityDtls> savedPlans = repo.saveAll(outList);
//		System.out.println("to be sent to controller" + savedPlans);
		return savedPlans;
	}

	@Override
	public List<String> getUniquePlanNames() {
		List<String> planNames = repo.getUniquePlanNames();
		return planNames;
	}

	@Override
	public List<String> getUniquePlanStatus() {
		List<String> planStatuses = repo.getUniquePlanStatus();
		return planStatuses;
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {
		EligibilityDtls dtls = new EligibilityDtls();
		if(request.getPlanStatus()!=null && !request.getPlanStatus().isBlank()) {
			dtls.setPlanStatus(request.getPlanStatus());
		}
		if(request.getPlanName()!=null && !request.getPlanName().isBlank()) {
			dtls.setPlanName(request.getPlanName());
		}
		Example<EligibilityDtls> example = Example.of(dtls);
		List<EligibilityDtls> firstFilteredList= repo.findAll(example);
//		System.out.println(dtls);
//		System.out.println(firstFilteredList);
		
		if(request.getPlanStartDate()!=null || request.getPlanEndDate()!=null) {
			firstFilteredList.removeIf(e->
			(request.getPlanStartDate()!=null &&
			(e.getPlanStartDate()==null || e.getPlanStartDate().isBefore(request.getPlanStartDate())))
			||
			(request.getPlanEndDate()!=null &&
			(e.getPlanEndDate()==null || e.getPlanEndDate().isAfter(request.getPlanEndDate())))
					);
		}
		List<SearchResponse> allResponses = new ArrayList<>();
		for (EligibilityDtls eligibilityDtls : firstFilteredList) {
			SearchResponse response = new SearchResponse();
			BeanUtils.copyProperties(eligibilityDtls, response);
			allResponses.add(response); 
			
		}
		return allResponses;
	}

	@Override
	public List<SearchResponse> search2(SearchRequest request) {
		List<SearchResponse> response = new ArrayList<>();
		System.out.println(request);
		EligibilityDtls queryBuliderExample = new EligibilityDtls();

		if (request.getPlanName() != null && !request.getPlanName().equals(""))
			queryBuliderExample.setPlanName(request.getPlanName());

		if (request.getPlanStatus() != null && !request.getPlanStatus().equals(""))
			queryBuliderExample.setPlanStatus(request.getPlanStatus());

		if (request.getPlanStartDate() != null)
			queryBuliderExample.setPlanStartDate(request.getPlanStartDate());

		if (request.getPlanEndDate() != null)
			queryBuliderExample.setPlanEndDate(request.getPlanEndDate());
		System.out.println(queryBuliderExample);
		Example<EligibilityDtls> example = Example.of(queryBuliderExample);
		List<EligibilityDtls> all = repo.findAll(example);
		for (EligibilityDtls eg : all) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(eg, sr);
			response.add(sr);

		}

		return response;

	}
	@Override
	public void generateExcel(HttpServletResponse respone,SearchRequest request) throws IOException {
		
		List<SearchResponse> all = search(request);
		XSSFWorkbook workbook =new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow headerRow = sheet.createRow(0);
		
		headerRow.createCell(0).setCellValue("S.no");
		headerRow.createCell(1).setCellValue("Name");
		headerRow.createCell(2).setCellValue("Mobile");
		headerRow.createCell(3).setCellValue("Gender");
		headerRow.createCell(4).setCellValue("Email");
		headerRow.createCell(5).setCellValue("SSN");
		headerRow.createCell(6).setCellValue("none");
		
		System.out.println(all);
		int i=1;
		for (SearchResponse entity : all) {
			
			XSSFRow dataRow = sheet.createRow(i);
			dataRow.createCell(0).setCellValue(i++);
			dataRow.createCell(1).setCellValue(entity.getName());
			dataRow.createCell(2).setCellValue(entity.getMobile());
			dataRow.createCell(3).setCellValue(entity.getGender());
			dataRow.createCell(4).setCellValue(entity.getEmail());
			dataRow.createCell(5).setCellValue(entity.getSsn());
		};
		ServletOutputStream outputStream = respone.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	@Override
	public void generatePdf(HttpServletResponse respone,SearchRequest request) throws DocumentException, IOException {
		List<SearchResponse> all = search(request);
		
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, respone.getOutputStream());
		
		document.open();
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		
		Paragraph p = new Paragraph("Search Report", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(p);
		PdfPTable pdfTable = new PdfPTable(6);
		pdfTable.setWidthPercentage(100f); 
		pdfTable.setWidths(new float[]{1.5f,3.0f,2.5f,1.8f,5.0f,3.2f});
		pdfTable.setSpacingBefore(10);
		
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5); 
		cell.setFixedHeight(25);
		font=FontFactory.getFont(FontFactory.HELVETICA);
		
		cell.setPhrase(new Phrase("S.no",font));
		pdfTable.addCell(cell);
		
		cell.setPhrase(new Phrase("Name", font));
		pdfTable.addCell(cell);
		
		cell.setPhrase(new Phrase("Mobile", font));
		pdfTable.addCell(cell);
		
		cell.setPhrase(new Phrase("Gender", font));
		pdfTable.addCell(cell);
		
		cell.setPhrase(new Phrase("Email", font));
		pdfTable.addCell(cell);
		 
		cell.setPhrase(new Phrase("SSN", font));
		pdfTable.addCell(cell);
		int i=1;
		for (SearchResponse entity : all) {
			
			pdfTable.addCell(String.valueOf(i++));
			pdfTable.addCell(entity.getName());
			pdfTable.addCell(String.valueOf(entity.getMobile()));
			pdfTable.addCell(entity.getGender());
			pdfTable.addCell(entity.getEmail());
			pdfTable.addCell(String.valueOf(entity.getSsn()));
		}
		document.add(pdfTable);
		document.close();
		
	}

}
