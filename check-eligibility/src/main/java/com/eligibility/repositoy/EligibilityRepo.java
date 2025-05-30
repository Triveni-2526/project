package com.eligibility.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eligibility.binding.EligibilityBinding;
import com.eligibility.entity.EligibilityEntity;

public interface EligibilityRepo extends JpaRepository<EligibilityEntity, Integer>{

	public EligibilityEntity findByCaseNum(Long caseNum);
	public boolean existsByCaseNum(Long caseNum);
}
