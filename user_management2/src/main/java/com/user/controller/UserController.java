package com.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.binding.ActivateAccount;
import com.user.binding.RegistrationRequest;
import com.user.binding.UserResponse;
import com.user.service.UserServiceImpl;

@RestController
public class UserController {

	public UserController(UserServiceImpl impl) {
		super();
		this.impl = impl;
	}

	private UserServiceImpl impl;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
		boolean registerUser = impl.registerUser(registrationRequest);
		if (registerUser) {
			return ResponseEntity.ok("Registration Success");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Registration Failed");
		}
	}
	
	@PutMapping("/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActivateAccount activateAccount) {
		String activateUser = impl.activateUser(activateAccount);
		switch(activateUser) {
			case "Account successfully activated":
				return ResponseEntity.ok(activateUser);
			case "New password and confirm password donot match":
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(activateUser);
			case "Wrong credentials":
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(activateUser);
			case "Email does not exist please register":
				 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(activateUser);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
		}		
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> allUsers = impl.getAllUsers();
		
		if (allUsers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allUsers);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
	    UserResponse response = impl.getUserById(userId);

	    if (response != null) {
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(null); // or a message object if you want
	    }
	}
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = impl.deleteUserById(userId);

		if (isDeleted) {
			return ResponseEntity.ok("User deleted successfully.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
	}

}
