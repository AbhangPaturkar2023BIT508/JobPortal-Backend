package com.jobportal.entity;

import java.util.Base64;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jobportal.dto.Certifications;
import com.jobportal.dto.Experience;
import com.jobportal.dto.ProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="profile")
public class Profile {
	@Id
	private Long id;
	private String name;
	private String email;
	private String jobTitle;
	private String company;
	private String location;
	private String about;
	private byte[] picture;
	private Long totalExp;
	private List<String>skills;
	private List<Experience>experience;
	private List<Certifications>certification;
	private List<Long> savedJobs;
	
	public ProfileDTO toDTO() {
		return new ProfileDTO(this.id, this.name, this.email, this.jobTitle, this.company, this.location, this.about, this.picture!=null?Base64.getEncoder().encodeToString(this.picture):null , this.totalExp, this.skills, this.experience,this.certification, this.savedJobs);
	}
}
