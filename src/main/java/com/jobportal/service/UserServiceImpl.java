package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportal.dto.LoginDTO;
import com.jobportal.dto.NotificationDTO;
import com.jobportal.dto.ResponseDTO;
import com.jobportal.dto.UserDTO;
import com.jobportal.entity.OTP;
import com.jobportal.entity.User;
import com.jobportal.exception.JobPortalException;
import com.jobportal.repository.OTPRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.utility.Data;
import com.jobportal.utility.Utilitites;

import jakarta.mail.internet.MimeMessage;

@Service(value="userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OTPRepository otpRepository;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public UserDTO registerUser(UserDTO userDto) throws JobPortalException {
		Optional<User> optional = userRepository.findByEmail(userDto.getEmail());
		if(optional.isPresent()) throw new JobPortalException("USER_FOUND");
		userDto.setProfileId(profileService.createProfile(userDto.getEmail()));
		userDto.setId(Utilitites.getNextSequence("users"));
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user  = userDto.toEntity();
		user = userRepository.save(user);
		return user.toDTO();
	}

	@Override
	public UserDTO loginUser(LoginDTO loginDto) throws JobPortalException {
		User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(()->new JobPortalException("USER_NOT_FOUND"));
		if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) throw new JobPortalException("INVALID_CREDENTIALS");
		return user.toDTO();
	}
	
	@Override
	public Boolean sendOtp(String email) throws Exception{
		userRepository.findByEmail(email).orElseThrow(()->new JobPortalException("USER_NOT_FOUND"));
		MimeMessage mm = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mm, true);
		message.setTo(email);
		message.setSubject("Your OTP Code");
		String genOtp = Utilitites.generateOtp();
		OTP otp = new OTP(email, genOtp, LocalDateTime.now());
		otpRepository.save(otp);
		message.setText(Data.getMessageBody(genOtp), true);
		mailSender.send(mm);
		return true;
	}

	@Override
	public Boolean verifyOtp(String email, String otp) throws JobPortalException {
		OTP otpEntity = otpRepository.findById(email).orElseThrow(()->new JobPortalException("OTP_NOT_FOUND"));
		if(!otpEntity.getOtpCode().equals(otp)) throw new JobPortalException("OTP_INCORRECT");
		return true;
	}

	@Scheduled(fixedRate = 60000)
	public void removeExpiredOTPs() {
		LocalDateTime expiry = LocalDateTime.now().minusMinutes(5);
		List<OTP> expiredOTPs = otpRepository.findByCreationTimeBefore(expiry);
		if(!expiredOTPs.isEmpty()) {
			otpRepository.deleteAll(expiredOTPs);
//			System.out.println("Removed "+expiredOTPs.size());
		}
		
	}

	@Override
	public ResponseDTO changePassword(LoginDTO loginDto) throws JobPortalException {
		User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(()->new JobPortalException("USER_NOT_FOUND"));
		user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
		userRepository.save(user);
		NotificationDTO notification = new NotificationDTO();
		notification.setUserId(user.getId());
		notification.setMessage("Password Reset Successfull");
		notification.setAction("Password Reset");
		notificationService.sendNotification(notification);
		return new ResponseDTO("Password Changed Successfully");
	}

	@Override
	public UserDTO getUserByEmail(String email) throws JobPortalException {
		return userRepository.findByEmail(email).orElseThrow(()->new JobPortalException("USER_NOT_FOUND")).toDTO();
	}
	
}
