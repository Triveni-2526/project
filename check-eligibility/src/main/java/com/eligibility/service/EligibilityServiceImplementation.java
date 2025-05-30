package com.eligibility.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.eligibility.binding.ChildrenBinding;
import com.eligibility.binding.CitizenData;
import com.eligibility.binding.CoTriggerResponse;
import com.eligibility.binding.EducationBinding;
import com.eligibility.binding.EligibilityBinding;
import com.eligibility.binding.IncomeBinding;
import com.eligibility.binding.SummaryBinding;
import com.eligibility.feign.AppRegistrationInterface;
import com.eligibility.feign.CorrespondenceFeign;
import com.eligibility.feign.DataCollectionFeign;
import com.eligibility.feign.PlanRegistrationInterface;
import com.eligibility.repositoy.EligibilityRepo;
import com.eligibility.entity.EligibilityEntity;

@Service
public class EligibilityServiceImplementation implements EligibilityService{
	
	
	private DataCollectionFeign collectionFeign;
	private AppRegistrationInterface appFeign;
	private PlanRegistrationInterface planFeign;
	private EligibilityRepo eligibilityRepo;
	private CorrespondenceFeign correspondenceFeign;
	
	public EligibilityServiceImplementation(DataCollectionFeign collectionFeign, AppRegistrationInterface appFeign,
			PlanRegistrationInterface planFeign, EligibilityRepo eligibilityRepo,
			CorrespondenceFeign correspondenceFeign) {
		super();
		this.collectionFeign = collectionFeign;
		this.appFeign = appFeign;
		this.planFeign = planFeign;
		this.eligibilityRepo = eligibilityRepo;
		this.correspondenceFeign = correspondenceFeign;
	}


	@Override
	public EligibilityBinding determineEligibility(Long caseNum) {
		if (eligibilityRepo.existsByCaseNum(caseNum)) {
	        EligibilityEntity alreadyExists = eligibilityRepo.findByCaseNum(caseNum);
	        EligibilityBinding binding = new EligibilityBinding();
	        BeanUtils.copyProperties(alreadyExists, binding);
	        return binding;
	    }
		SummaryBinding summary = collectionFeign.getSummary(caseNum).getBody();
		System.out.println(summary);
		Integer appId=null;
		String planName=null;
		if (summary!=null) {
			planName=summary.getPlanName();
			appId = summary.getAppId();
			System.out.println(appId);
		}
		Integer age = 0;
		CitizenData citizenData = appFeign.findCitizenById(appId);
		System.out.println("citiZen"+citizenData);
		if(citizenData!=null) {
			LocalDate dob = citizenData.getDob();
			age = Period.between(dob, LocalDate.now()).getYears();	
		}
		EligibilityBinding executePlanConditions = executePlanConditions(summary,planName,age);

		EligibilityEntity elgEntity = new EligibilityEntity();
		BeanUtils.copyProperties(executePlanConditions,elgEntity);
		elgEntity.setCaseNum(caseNum);
		elgEntity.setHolderName(citizenData.getFullName());
		elgEntity.setHolderSsn(citizenData.getSsn());
		eligibilityRepo.save(elgEntity);
		
		CoTriggerResponse triggerResponse = new CoTriggerResponse();
		triggerResponse.setCaseNum(caseNum);
		triggerResponse.setTrgStatus("Pending");
		correspondenceFeign.saveTrigger(triggerResponse);
		
		BeanUtils.copyProperties(elgEntity,executePlanConditions);
		return executePlanConditions;
	}
	private EligibilityBinding executePlanConditions(SummaryBinding summary,String planNmae,Integer age) {
		EligibilityBinding response = new EligibilityBinding();
		response.setPlanName(planNmae);
		
		IncomeBinding income = summary.getIncome();
		if("SNAP".equals(planNmae)) {
			Double empIncome= income.getEmpIncome();
			if(empIncome<=300) {
				response.setPlanStatus(true);
			}else {
				response.setPlanStatus(false);
				response.setDenialReason("High Income");
			}
		}else if("CCAP".equals(planNmae)) {
			Boolean ageCondition = true;
			Boolean kidCountCondition = false;
			
			List<ChildrenBinding> childrens = summary.getChildren();
			if(!childrens.isEmpty()) {
				kidCountCondition=true;
				for (ChildrenBinding child : childrens) {
					Integer childAge = child.getChildAge();
					if(childAge>16) {
						ageCondition = false;
						break;
					}
				}
			}if(income.getEmpIncome()<=300 && ageCondition && kidCountCondition) {
				response.setPlanStatus(true);
			}else {
				response.setPlanStatus(false);
				response.setDenialReason("Not staisying business rules");
			}
		}else if("MEDICARE".equals(planNmae)) {
			if(age >65) {
				response.setPlanStatus(true);
			}else {
				response.setPlanStatus(false);
				response.setDenialReason("Age less than 65");
			}
		}else if("NJW".equals(planNmae)) {
			EducationBinding education = summary.getEducation();
			Integer graduationYear = education.getGraduationYear();
			Integer currentYear = LocalDate.now().getYear();
			if(income.getEmpIncome()<=0 && graduationYear<currentYear) {
				response.setPlanStatus(true);
			}else {
				response.setPlanStatus(false);
				response.setDenialReason("Not Eligible");
			}
		}else if("MEDICATE".equals(planNmae)) {
			Double empIncome= income.getEmpIncome();
			Double propertyIncome = income.getPropertyIncome();
			if(empIncome<=300 && propertyIncome<=0) {
				response.setPlanStatus(true);
			}else {
				response.setPlanStatus(false);
				response.setDenialReason("High Income");
			}
		}
		if(response.getPlanStatus()==true) {
			response.setPlanStartDate(LocalDate.now());
			response.setPlanEndDate(LocalDate.now().plusMonths(6));
			response.setBenefitAmt(350.00);
		}
		return response;
		
	}


	@Override
	public EligibilityBinding EligibilityByCaseNum(Long caseNum) {
		EligibilityEntity eligibilityEntity = eligibilityRepo.findByCaseNum(caseNum);
		EligibilityBinding eligibilityBinding = new EligibilityBinding();
		BeanUtils.copyProperties(eligibilityEntity, eligibilityBinding);
		return eligibilityBinding;
	}
	@Override
	public List<EligibilityBinding> EligibleCitizens() {
		List<EligibilityEntity> all = eligibilityRepo.findAll();
		List<EligibilityEntity> approveCitizens = all.stream().filter(each->Boolean.TRUE.equals(each.getPlanStatus())).collect(Collectors.toList());
		List<EligibilityBinding> approved = approveCitizens.stream().map(entity -> {
            EligibilityBinding binding = new EligibilityBinding();
            BeanUtils.copyProperties(entity, binding);
            return binding;
        }).collect(Collectors.toList());
		return approved;
	}
 
}
