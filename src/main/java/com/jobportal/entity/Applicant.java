package com.jobportal.entity;

import java.time.LocalDateTime;
import java.util.Base64;

import com.jobportal.dto.ApplicantDTO;
import com.jobportal.dto.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
	private Long applicantId;
	private String email;
	private Long phone;
	private String website;
	private byte[] resume;
	private String coverLetter;
	private LocalDateTime timeStamp;
	private ApplicationStatus applicationStatus;
	private LocalDateTime interviewTime;
	
	public ApplicantDTO toDTO() {
		return new ApplicantDTO(this.applicantId, this.email, this.phone, this.website, this.resume!=null?Base64.getEncoder().encodeToString(this.resume):null, this.coverLetter, this.timeStamp, this.applicationStatus, this.interviewTime);
	}
}
