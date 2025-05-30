package com.report.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.report.entity.EligibilityDtls;

public interface EligibilityDtlsRepo extends JpaRepository<EligibilityDtls, Integer> {
		
/*	@Query("SELECT DISTINCT(PLAN_STATUS) FROM ELIGIBILITY_DTLS"+
		WHERE()+
		AND()+
		AND())*/
	@Query("SELECT DISTINCT(planStatus) FROM EligibilityDtls")
	public List<String> getUniquePlanStatus();
	
	@Query(value="SELECT DISTINCT PLAN_NAME FROM ELIGIBILITY_DTLS", nativeQuery = true)
	public List<String> getUniquePlanNames();

}
