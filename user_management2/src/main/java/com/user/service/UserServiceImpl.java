package com.user.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginRequest;
import com.user.binding.RegistrationRequest;
import com.user.binding.UserResponse;
import com.user.entity.User;
import com.user.mapper.UserMapper;
import com.user.repository.UserRepo;
import com.user.utils.EmailUtils;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	
	private UserRepo repo;
	private EmailUtils emailUtils;
	private UserMapper userMapper;
	Logger logger = LogManager.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepo repo, EmailUtils emailUtils, UserMapper userMapper) {
		super();
		this.repo = repo;
		this.emailUtils = emailUtils;
		this.userMapper = userMapper;
		logger.info("UserServiceImpl initialized with dependencies");
	}

	@Override
	@Transactional
	public Boolean registerUser(RegistrationRequest registrationRequest) {
		logger.info("Processing registation request for email:{}", registrationRequest.getEmail());
		Optional<User> byEmail = repo.findByEmail(registrationRequest.getEmail());

		if (byEmail.isPresent()) {
			logger.info("user email aready exists");
			return false;
		}
		User user = userMapper.toEntity(registrationRequest);
//		BeanUtils.copyProperties(registrationRequest, user);
		user.setAccStatus("In-Active");
		user.setPassword(generateRandomPwd());

		User savedUser = repo.save(user);

		logger.info("User saved sucessfully with id {}" + savedUser.getUserId());
		Boolean sendEmail = false;

		if (savedUser.getUserId() != null) {
			String subject = "Registration successful!";
			String fileName = "email-template.html";
			String body = readEmailBody(user.getFullName(), user.getPassword(), fileName);
			sendEmail = emailUtils.sendEmail(user.getEmail(), subject, body);
		}

		return sendEmail;
	}

	@Override
	public String activateUser(ActivateAccount activateAccount) {
		Optional<User> byEmail = repo.findByEmail(activateAccount.getEmail());
		logger.info("Processing account activation request for email: {}", activateAccount.getEmail());
		if (byEmail.isPresent()) {
			User user = byEmail.get();
			logger.debug("Found user for activation: {}", user.getUserId());
			if (activateAccount.getTempPassword().equals(user.getPassword())) {
				if (activateAccount.getNewPassword().equals(activateAccount.getConfirmPassword())) {
					user.setAccStatus("Active");
					user.setPassword(activateAccount.getNewPassword());
					repo.save(user);
					logger.info("Account activated successfully: {}", user.getUserId());
					return "Account successfully activated";
				} else {
					logger.warn("Password mismatch during activation for user: {}", user.getUserId());
					return "New password and confirm password donot match";
				}
			} else {
				logger.warn("Invalid temporary password provided for user: {}", user.getUserId());
				return "Wrong credentials";
			}
		} else {
			logger.warn("Activation attempted for non-existent email: {}", activateAccount.getEmail());
			return "Email does not exist please register";
		}
	}

	@Override
	public List<UserResponse> getAllUsers() {
//		List<UserResponse> users = new ArrayList<>();
		List<User> allUsers = repo.findAll();
//		for (User user : allUsers) {
//			UserResponse response = new UserResponse();
//			BeanUtils.copyProperties(user, response);
//			users.add(response);
//		}
		List<UserResponse> users = userMapper.toUserResponseList(allUsers);
		logger.info("Found {} users", users.size());
		return users;
	}

	@Override
	public UserResponse getUserById(Integer userId) {
		logger.info("Retrieving user by ID: {}", userId);
		Optional<User> byId = repo.findById(userId);

		if (byId.isPresent()) {
			logger.debug("Found user with ID: {}", userId);
			User user = byId.get();
			UserResponse response = userMapper.toUserResponse(user);
//			BeanUtils.copyProperties(user, response);
			return response;
		}
		logger.warn("User not found with Id{}", userId);
		return null;
	}

	@Override
	public UserResponse getUserByEmail(String email) {
		logger.info("Retrieving user by email: {}", email);
		Optional<User> byEmail = repo.findByEmail(email);
		if (byEmail.isPresent()) {

			User user = byEmail.get();
			logger.debug("Found user with email: {}", email);
			UserResponse response = userMapper.toUserResponse(user);
//			BeanUtils.copyProperties(user, response);
			return response;
		}
		logger.warn("User not found with email: {}", email);
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		logger.info("Attempting to delete user with ID: {}", userId);
		try {
			if (repo.existsById(userId)) {
				repo.deleteById(userId);
				logger.error("user successfully deleted" + userId);
				return true;
			} else {
				logger.error("user do not exist" + userId);
				return false;
			}

		} catch (Exception e) {
			logger.error("Exception Occured", e);
			return false;
		}

	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		logger.info("Changing account status to '{}' for user ID: {}", accStatus, userId);
		Optional<User> byId = repo.findById(userId);
		if (byId.isPresent()) {
			User user = byId.get();
			user.setAccStatus(accStatus);
			repo.save(user);
			logger.info("Successfully updated statusfor user id {}", userId);
			return true;
		}
		logger.warn("Userid {} do not exist", userId);
		return false;
	}

	@Override
	public String login(LoginRequest loginRequest) {
		logger.info("Processing login request for email: {}", loginRequest.getEmail());
		Optional<User> byEmail = repo.findByEmail(loginRequest.getEmail());

		if (byEmail.isEmpty()) {
			logger.warn("Login attempted with non-existent email: {}", loginRequest.getEmail());
			return "Account does not exist please register";
		}
		User user = byEmail.get();
		if (user.getAccStatus().equals("In-Active")) {
			logger.warn("Login attempted for inactive account: {}", loginRequest.getEmail());
			return "Account is not activated please activate and tr logging in";
		}
		if (!user.getPassword().equals(loginRequest.getPassword())) {
			logger.warn("Login failed due to incorrect password for email: {}", loginRequest.getEmail());
			return "Wrong credentials";
		}
		logger.info("Login successful for email: {}", loginRequest.getEmail());
		return "Successfully logged in";
	}

	@Override
	public String forgotPassword(String email) {
		logger.info("Processing forgot password request for email: {}", email);
		Optional<User> byEmail = repo.findByEmail(email);

		if (byEmail.isEmpty()) {
			logger.warn("Forgot password requested for non-existent email: {}", email);
			return "Account does not exist please register";
		}
		User user = byEmail.get();
		String fileName = "recover-pwd-body.txt";
		String body = readEmailBody(user.getFullName(), user.getPassword(), fileName);
		String subject = "Password recovery";
		boolean sendEmail = emailUtils.sendEmail(user.getEmail(), subject, body);
//		if (user.getAccStatus().equals("In-Active")) {
//			return "Account is not activated please activate and try logging in";
//		}

		if (sendEmail) {
			logger.info("Password recovery email sent to: {}", email);
			return "Password sent to your registered mail";
		}
		logger.error("Failed to send password recovery email to: {}", email);
		return null;
	}

	private String readEmailBody(String fullName, String password, String fileName) {
		logger.debug("Reading email template from file: {}", fileName);
		
		String url = "http://localhost:8080/swagger-ui/index.html#/user-controller/activateUser";
		String mailBody = "";

		try (FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);) {
			StringBuilder bodyBuilder = new StringBuilder();
			String line = bufferedReader.readLine();

			while (line != null) {
				bodyBuilder.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			mailBody = bodyBuilder.toString();
			mailBody = mailBody.replace("${name}", fullName);
			mailBody = mailBody.replace("${tempPassword}", password);
			mailBody = mailBody.replace("${activationLink}", url);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.error("Error reading email template file: {}", fileName, e1);
			e1.printStackTrace();
		}
		return mailBody;
	}

	private String generateRandomPwd() {
		final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
		final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
		final String DIGITS = "0123456789";
		final String SPECIAL_CHARS = "!@#$%^&*()_-+=<>?";
		final String ALL_CHARS = CHAR_LOWERCASE + CHAR_UPPERCASE + DIGITS + SPECIAL_CHARS;

		int length = 8;
		StringBuilder password = new StringBuilder();
		Random random = new Random();

		password.append(CHAR_LOWERCASE.charAt(random.nextInt(CHAR_LOWERCASE.length())));
		password.append(CHAR_UPPERCASE.charAt(random.nextInt(CHAR_UPPERCASE.length())));
		password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

		for (int i = 4; i < length; i++) {
//			ALL_CHARS[0];
			password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
		}
		char[] passwordArray = password.toString().toCharArray();

		for (int i = 0; i < passwordArray.length; i++) {
			int index = random.nextInt(passwordArray.length);

			char temp = passwordArray[i];
			passwordArray[i] = passwordArray[index];
			passwordArray[index] = temp;
		}
		logger.debug("Random password generated successfully");
		return password.toString();
	}
}
