package com.sjkb.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.JobEntity;
import com.sjkb.models.JobCardModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ContactRepository contactRepository;

    @Override
    public void createJob(String jobid, String designer, String pocId) {
        JobEntity job = new JobEntity();
        job.setJobid(jobid);
        job.setPocId(pocId);
        job.setCreateDate(new Timestamp(System.currentTimeMillis()));
        job.setActivityDate(job.getCreateDate());
        job.setUser(designer);
        job.setBudget(0);
        job.setContext("na");
        job.setState("Lead");
        jobRepository.save(job);
    }

    @Override
    public List<JobCardModel> getAllJobsForUser(String empid) {
        List<JobCardModel> result = new ArrayList<>();
        List<JobEntity> userjobs = jobRepository.findAllByUser(empid);
        for (JobEntity job : userjobs) {
            JobCardModel card = new JobCardModel();
            Optional<ContactEntity> contactOpt = contactRepository.findById(job.getPocId());
            if (contactOpt.isPresent()) {
                card.setTitle(contactOpt.get().getLastname());
            }
            card.setId(job.getJobid());
            card.setStatus(job.getState());
            card.setStatusDate(job.getActivityDate().toString().split(" ")[0]);
            card.setColor("lightblue");
            result.add(card);
        }
        return result;
        
	}
}