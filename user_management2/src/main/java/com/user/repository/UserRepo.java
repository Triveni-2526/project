package com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	public Optional<User> findByEmail(String email);
}
