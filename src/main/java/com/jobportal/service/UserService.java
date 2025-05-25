package com.jobportal.service;

import com.jobportal.dto.LoginDTO;
import com.jobportal.dto.ResponseDTO;
import com.jobportal.dto.UserDTO;
import com.jobportal.exception.JobPortalException;



public interface UserService {
	public UserDTO registerUser(UserDTO userDto) throws JobPortalException;

	public UserDTO loginUser(LoginDTO loginDto) throws JobPortalException;
	
	public Boolean sendOtp(String email) throws Exception;

	public Boolean verifyOtp(String email, String otp) throws JobPortalException;

	public ResponseDTO changePassword(LoginDTO loginDto) throws JobPortalException;
	
	public UserDTO getUserByEmail(String email) throws JobPortalException;
}
