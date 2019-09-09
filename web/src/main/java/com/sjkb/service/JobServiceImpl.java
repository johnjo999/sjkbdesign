package com.sjkb.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.JobEntity;
import com.sjkb.entities.JobEventEntity;
import com.sjkb.models.AssignExpenseModel;
import com.sjkb.models.JobAttributeModel;
import com.sjkb.models.JobCardModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.JobEventRepository;
import com.sjkb.repositores.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    JobEventRepository jobEventRepository;

    @Override
    public void createJob(String jobid, String designer, String pocId) {
        JobEntity job = new JobEntity();
        job.setJobid(jobid);
        job.setPocId(pocId);
        job.setCreateDate(new Timestamp(System.currentTimeMillis()));
        job.setActivityDate(job.getCreateDate());
        job.setUser(designer);
        job.setBudget(0);
        job.setQuote(0);
        job.setContext("na");
        job.setState("new");
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

    @Override
    public JobEntity getJobForFolder(String folder) {
        Optional<JobEntity> job = jobRepository.findById(folder);
        if (job.isPresent())
            return job.get();
        return new JobEntity();
    }

    @Override
    public void addExpense(String expense, AssignExpenseModel expenseModel) {
        Optional<JobEntity> jobOptional = jobRepository.findById(expenseModel.getFolder());
        JobEntity job = null;
        if (jobOptional.isPresent()) {
            job = jobOptional.get();
        } else
            return;
        if (expenseModel.getLowEstimate() > expenseModel.getHighEstimate()) {
            int le = expenseModel.getLowEstimate();
            expenseModel.setLowEstimate(expenseModel.getHighEstimate());
            expenseModel.setHighEstimate(le);
        }
        switch (expense) {
        case "contractor":
        case "installer":
            JobEventEntity newEvent = new JobEventEntity();
            newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
            newEvent.setJobid(job.getJobid());
            newEvent.setCreatorId(job.getUser());
            newEvent.setObjid(expenseModel.getContId());
            newEvent.setType(expense);
            if (expenseModel.getQuote() > 0) {
                newEvent.setLowEnd(expenseModel.getQuote());
                newEvent.setHighEnd(expenseModel.getQuote());
            } else {
                newEvent.setLowEnd(expenseModel.getLowEstimate());
                newEvent.setHighEnd(expenseModel.getHighEstimate());
            }
            if (expenseModel.getDate() != null) {
                newEvent.setScheduled(expenseModel.getTimestamp());
            }
            jobEventRepository.save(newEvent);
            break;
        }
    }

    @Override
    public JobAttributeModel getAttributesFor(JobEntity job) {
        JobAttributeModel result = new JobAttributeModel();
        List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidOrderByTimestamp(job.getJobid());
        if (jobEvents != null && jobEvents.size() > 0) {
            for (JobEventEntity jevent : jobEvents) {
                if (jevent.getType().equals("contractor")) {
                    Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        ContactEntity contractor = optionalContact.get();
                        result.setContractor(contractor.getCompany());
                        result.setContractorId(contractor.getUid());
                    }

                }
                if (jevent.getType().equals("installer")) {
                    Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        ContactEntity contractor = optionalContact.get();
                        result.setInstaller(contractor.getCompany());
                        result.setInstallerId(contractor.getUid());
                    }

                }
            }
        }
        return result;
    }

    @Override
    public AssignExpenseModel getCurrentExpenseFor(String jobid, String role) {
        List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidAndTypeOrderByTimestamp(jobid, role);
        int i = jobEvents.size();
        if (i > 0) {
           return new AssignExpenseModel(jobEvents.get(i-1));
        }
        return null;
    }

    @Override
    public String getJobHistory(JobEntity job) {
        String result = "<span>";
        List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidOrderByTimestamp(job.getJobid());
        for (JobEventEntity jobEvent : jobEvents) {
            result += jobEvent.getMessage()+"<br/>";
        }
        return result;
    }
}