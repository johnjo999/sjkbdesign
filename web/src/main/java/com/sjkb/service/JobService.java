package com.sjkb.service;

import java.util.List;

import com.sjkb.models.JobCardModel;

public interface JobService {

	void createJob(String jobid, String designer, String jobPocId);

	public List<JobCardModel> getAllJobsForUser(String empid);

}