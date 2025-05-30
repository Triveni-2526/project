package com.issuance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.issuance.entity.BenefitIssued;

public interface BenefitIssuedRepo extends JpaRepository<BenefitIssued, Integer>{

}
