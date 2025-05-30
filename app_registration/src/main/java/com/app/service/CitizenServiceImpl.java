package com.app.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.binding.CitizenData;
import com.app.entity.Citizen;
import com.app.repository.CitizenRepository;

@Service
public class CitizenServiceImpl implements CitizenService{
	
	CitizenRepository citizenRepository;

	public CitizenServiceImpl(CitizenRepository citizenRepository) {
		super();
		this.citizenRepository = citizenRepository;
	}

	@Override
	public Integer createApplication(CitizenData citizenData) {
		String endpint ="http://localhost:8091/getState/{ssn}";
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> forEntity = restTemplate.getForEntity(endpint, String.class,citizenData.getSsn());
		String stateName = forEntity.getBody();
		
		Optional<Citizen> bySsn = citizenRepository.findBySsn(citizenData.getSsn());
		if(stateName.equals("New Jersey") && bySsn.isEmpty()) {
			Citizen citizen = new Citizen();
			BeanUtils.copyProperties(citizenData, citizen);
			citizen.setStateName(stateName);
			citizenRepository.save(citizen);
			return citizen.getAppId();
		}
		return 0;
	}

	@Override
	public Boolean findById(Integer appId) {
		Optional<Citizen> byIdOptional = citizenRepository.findById(appId);
		if(byIdOptional.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public CitizenData findCitizenById(Integer appId) {
		Optional<Citizen> byIdOptional = citizenRepository.findById(appId);
		if(byIdOptional.isPresent()) {
			CitizenData citizenData = new CitizenData();
			 Citizen citizen = byIdOptional.get();
			 BeanUtils.copyProperties(citizen, citizenData);
			 return citizenData;
		}
		return null;
	}

}
