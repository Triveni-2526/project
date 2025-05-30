package com.correspondence.service;

import com.correspondence.binding.CoResponse;
import com.correspondence.binding.CoTriggerBinding;

public interface CoTriggerService {

	public CoTriggerBinding saveTrigger(CoTriggerBinding triggerBinding);
	public CoResponse processPendingTriggers();
	
}
