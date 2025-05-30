package com.app.service;

import com.app.binding.CitizenData;

public interface CitizenService {

	public Integer createApplication(CitizenData citizenData);
	public Boolean findById(Integer appId);
	public CitizenData findCitizenById(Integer appId);
}
