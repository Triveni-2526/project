package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Citizen;

public interface CitizenRepository extends JpaRepository<Citizen, Integer> {

	public Optional<Citizen> findBySsn(Long ssn);
}
