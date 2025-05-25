package com.jobportal.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jobportal.dto.UserDTO;
import com.jobportal.exception.JobPortalException;
import com.jobportal.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			UserDTO userDto = userService.getUserByEmail(email);
			return new CustomUserDetails(userDto.getId(), email, userDto.getName(),userDto.getProfileId(),  userDto.getPassword(),  userDto.getAccountType(), new ArrayList<>());
		} catch (JobPortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
