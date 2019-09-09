package com.sjkb.service;

import java.util.List;

import com.sjkb.entities.JobEntity;
import com.sjkb.models.AssignExpenseModel;
import com.sjkb.models.JobAttributeModel;
import com.sjkb.models.JobCardModel;

public interface JobService {

	void createJob(String jobid, String designer, String jobPocId);

	public List<JobCardModel> getAllJobsForUser(String empid);

	public JobEntity getJobForFolder(String folder);

	void addExpense(String expense, AssignExpenseModel expenseModel);

	JobAttributeModel getAttributesFor(JobEntity job);

	AssignExpenseModel getCurrentExpenseFor(String jobid, String role);

	String getJobHistory(JobEntity job);

}