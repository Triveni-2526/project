package com.user.service;

import java.util.List;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginRequest;
import com.user.binding.RegistrationRequest;
import com.user.binding.UserResponse;

public interface UserService {
	
	
	public Boolean registerUser(RegistrationRequest registrationRequest);
	
	public String activateUser(ActivateAccount activateAccount);
	
	public List<UserResponse> getAllUsers();
	
	public UserResponse getUserById(Integer userId);
	
	public UserResponse getUserByEmail(String email);
	
	public boolean deleteUserById(Integer userId);
	
	public boolean changeAccountStatus(Integer userId, String accStatus);
	
	public String login(LoginRequest loginRequest);
	
	public String forgotPassword(String email);
	

}
