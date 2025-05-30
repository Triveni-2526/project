package com.issuance.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.issuance.binding.CaseResponse;
import com.issuance.binding.CitizenData;
import com.issuance.binding.EligibilityBinding;
import com.issuance.entity.BenefitIssued;
import com.issuance.feign.AppRegistrationFeign;
import com.issuance.feign.DataCollectionFeign;
import com.issuance.feign.EligibilityFeign;
import com.issuance.repository.BenefitIssuedRepo;
import com.issuance.utils.EmailUtils;

@Service
public class BenefitIssuanceServiceImpl implements BenefitIssuanceService {

	private EmailUtils emailUtils;
	private AppRegistrationFeign appRegistrationFeign;
	
	BenefitIssuedRepo benefitIssuedRepo;
	
	private DataCollectionFeign collectionFeign;
	
	private EligibilityFeign eligibilityFeign;
	
	public BenefitIssuanceServiceImpl(EmailUtils emailUtils, AppRegistrationFeign appRegistrationFeign,
			BenefitIssuedRepo benefitIssuedRepo, DataCollectionFeign collectionFeign,
			EligibilityFeign eligibilityFeign) {
		super();
		this.emailUtils = emailUtils;
		this.appRegistrationFeign = appRegistrationFeign;
		this.benefitIssuedRepo = benefitIssuedRepo;
		this.collectionFeign = collectionFeign;
		this.eligibilityFeign = eligibilityFeign;
	}
	@Override
	public void sendMail() {
		List<EligibilityBinding> eligibleCitizens = eligibilityFeign.EligibleCitizens();
		for (EligibilityBinding eligibilityBinding : eligibleCitizens) {
			BenefitIssued benefitIssued = new BenefitIssued();
			CaseResponse caseResponse = collectionFeign.getCaseEntity(eligibilityBinding.getCaseNum());
			CitizenData citizenById = appRegistrationFeign.findCitizenById(caseResponse.getAppId());
			BeanUtils.copyProperties(eligibilityBinding, benefitIssued);
			benefitIssued.setEmail(citizenById.getEmail());
			benefitIssued.setMobile(citizenById.getMobile());
			benefitIssuedRepo.save(benefitIssued);
		}
		List<BenefitIssued> benefitToIssue = benefitIssuedRepo.findAll();
		try {
            File excelFile = generateExcelFile(benefitToIssue);

            // Send email with attachment
            String subject = "HIS Eligibility Info";
            String body = "Please find attached the benefit issuance report.";
            String recipient = "triveniboppudi14@gmail.com"; // Replace with dynamic email if needed

            emailUtils.sendEmail(subject, body, recipient, excelFile);

            // Clean up temp file
            if (excelFile.exists()) {
                excelFile.delete();
            }

        } catch (IOException e) {
            e.printStackTrace(); // Replace with proper logging in real application
        }
		
		
	}
	private File generateExcelFile(List<BenefitIssued> benefitList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Benefit Report");

        // Header Row
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("S.No");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Mobile");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Plan Name");
        headerRow.createCell(5).setCellValue("Start Date");
        headerRow.createCell(6).setCellValue("End Date");
        headerRow.createCell(7).setCellValue("Benefit Amt");
        headerRow.createCell(8).setCellValue("SSN");

        int rowIndex = 1;
        for (BenefitIssued entity : benefitList) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex - 1);
            row.createCell(1).setCellValue(entity.getHolderName());
            row.createCell(2).setCellValue(entity.getMobile());
            row.createCell(3).setCellValue(entity.getEmail());
            row.createCell(4).setCellValue(entity.getPlanName());
            row.createCell(5).setCellValue(String.valueOf(entity.getPlanStartDate()));
            row.createCell(6).setCellValue(String.valueOf(entity.getPlanEndDate()));
            row.createCell(7).setCellValue(entity.getBenefitAmt());
            row.createCell(8).setCellValue(entity.getHolderSsn());
        }

        File file = File.createTempFile("benefit-report-", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();

        return file;
    }


}
