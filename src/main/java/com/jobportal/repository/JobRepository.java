package com.jobportal.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobportal.entity.Job;

public interface JobRepository extends MongoRepository<Job, Long> {
	public List<Job> findByPostedBy(Long PostedBy);
}
