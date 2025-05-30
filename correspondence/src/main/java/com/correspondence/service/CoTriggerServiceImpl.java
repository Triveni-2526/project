package com.correspondence.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.correspondence.binding.CaseResponse;
import com.correspondence.binding.CitizenData;
import com.correspondence.binding.CoResponse;
import com.correspondence.binding.CoTriggerBinding;
import com.correspondence.binding.EligibilityBinding;
import com.correspondence.entity.CoTrigger;
import com.correspondence.feign.AppRegistrationFeign;
import com.correspondence.feign.DataCollectionFeign;
import com.correspondence.feign.EligibilityFeign;
import com.correspondence.repository.CoTriggerRepo;
import com.correspondence.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CoTriggerServiceImpl implements CoTriggerService{
	
	private CoTriggerRepo triggerRepo;
	
	@Autowired
    private EligibilityFeign eligibilityFeign;
	
	@Autowired
	private DataCollectionFeign collectionFeign;
	
	@Autowired
	private AppRegistrationFeign appRegistrationFeign;
	
	@Autowired
	private EmailUtils emailUtils;
	

	public CoTriggerServiceImpl(CoTriggerRepo triggerRepo) {
		super();
		this.triggerRepo = triggerRepo;
	}

	@Override
	public CoTriggerBinding saveTrigger(CoTriggerBinding triggerBinding) {
		Long caseNum = triggerBinding.getCaseNum();
		Optional<CoTrigger> optionalTrigger = triggerRepo.findByCaseNum(caseNum);
		if(!optionalTrigger.isPresent()) {
			CoTrigger coTrigger = new CoTrigger();
            BeanUtils.copyProperties(triggerBinding, coTrigger);
            CoTrigger saved = triggerRepo.save(coTrigger);

            CoTriggerBinding result = new CoTriggerBinding();
            BeanUtils.copyProperties(saved, result);
            return result;
		}
		return null;
	}

	@Override
	public CoResponse processPendingTriggers() {
		CoResponse response = new CoResponse();
		List<CoTrigger> byTrgStatus = triggerRepo.findByTrgStatus("Pending");
		System.out.println(byTrgStatus);
		response.setTotalTriggers(Long.valueOf(byTrgStatus.size()));
		// TODO Auto-generated method stub
		Long success = 0l;
		Long failed = 0l;
		for (CoTrigger trigger : byTrgStatus) {
			EligibilityBinding eligibilityBinding = eligibilityFeign.getEligibilityByCaseNum(trigger.getCaseNum()).getBody();
			CaseResponse caseResponse = collectionFeign.getCaseEntity(trigger.getCaseNum());
			Integer appId = caseResponse.getAppId();
			CitizenData citizenById = appRegistrationFeign.findCitizenById(appId);
			if (citizenById!=null) {
				try {
					generateAndSendPdf(eligibilityBinding, citizenById);
					success++;
				} catch (Exception e) {
					e.printStackTrace();
					failed++;
				}
				
			}
		}
		response.setSuccTriggers(success);
		response.setFailedTriggers(failed);
		return response;
	}
	private void generateAndSendPdf(EligibilityBinding eligData, CitizenData appEntity) throws Exception {
		Document document = new Document(PageSize.A4);
		FileOutputStream fos = null;
		File file = new File(eligData.getCaseNum()+".pdf");
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		PdfWriter.getInstance(document, fos);
		
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph p = new Paragraph("ELIGIBILITY REPORT", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(p);
		
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] {2.0f, 2.2f, 2.5f, 3.0f, 3.0f, 2.5f, 2.8f});
		table.setSpacingBefore(10);
		
		PdfPCell cell = new PdfPCell();
		
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Citizen Name", font));
		table.addCell(cell);
			
		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Plan Start Date", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Plan End Date", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Benefot Amount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Denial Reason", font));
		table.addCell(cell);
			table.addCell(appEntity.getFullName());
			table.addCell(eligData.getPlanName());
			Boolean planStatus = eligData.getPlanStatus();
			table.addCell(planStatus+"");
			table.addCell(eligData.getPlanStartDate()+"");
			table.addCell(eligData.getPlanEndDate()+"");
			table.addCell(eligData.getBenefitAmt()+"");
			table.addCell(eligData.getDenialReason()+"");
		document.add(table);
		document.close();
		
		
		String subject = "HIS Eligibility Info";
		String body = "HIS Eligibility Info";
		emailUtils.sendEmail(subject, body,appEntity.getEmail() , file);
		updateTrigger(eligData.getCaseNum(), file);
		
		if (file.exists()) {
		    file.delete();
		}
	}
	
	private void updateTrigger(Long caseNum, File file) throws Exception {
		CoTrigger coTriggerEntity = triggerRepo.findByCaseNum(caseNum).get();
		
		byte[] arr = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(arr);
		
		coTriggerEntity.setCoPdf(arr);
		coTriggerEntity.setTrgStatus("Completed");
		triggerRepo.save(coTriggerEntity);
		
		fis.close();
	}

}
