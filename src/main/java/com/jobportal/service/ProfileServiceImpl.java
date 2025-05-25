package com.jobportal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.dto.ProfileDTO;
import com.jobportal.entity.Profile;
import com.jobportal.exception.JobPortalException;
import com.jobportal.repository.ProfileRepository;
import com.jobportal.utility.Utilitites;

@Service(value="profileService")
public class ProfileServiceImpl implements ProfileService{
	@Autowired
	ProfileRepository profileRepository;
	
	@Override
	public Long createProfile(String email) throws JobPortalException {
		Profile profile = new Profile();
		profile.setId(Utilitites.getNextSequence("profiles"));
		profile.setJobTitle("");
		profile.setEmail(email);
		profile.setSkills(new ArrayList<>());
		profile.setExperience(new ArrayList<>());
		profile.setCertification(new ArrayList<>());
		profileRepository.save(profile);
		return profile.getId();
	}

	@Override
	public ProfileDTO getProfile(Long id) throws JobPortalException {
		return profileRepository.findById(id).orElseThrow(()->new JobPortalException("PROFILE_NOT_FOUND")).toDTO();
	}

	@Override
	public ProfileDTO updateProfile(ProfileDTO profileDTO) throws JobPortalException {
		profileRepository.findById(profileDTO.getId()).orElseThrow(()->new JobPortalException("PROFILE_NOT_FOUND")).toDTO();
		profileRepository.save(profileDTO.toEntity());
		return profileDTO;
	}

	@Override
	public List<ProfileDTO> getAllProfiles() {
		return profileRepository.findAll().stream().map((x)->x.toDTO()).toList();	
	}

}
