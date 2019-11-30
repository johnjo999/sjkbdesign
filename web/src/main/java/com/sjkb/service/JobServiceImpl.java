package com.sjkb.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadUploader;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.InvoiceEntity;
import com.sjkb.entities.InvoiceItemEntity;
import com.sjkb.entities.JobEntity;
import com.sjkb.entities.JobEventEntity;
import com.sjkb.entities.JobExpenseEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.models.AssignExpenseModel;
import com.sjkb.models.jobs.AddInvoiceModel;
import com.sjkb.models.jobs.JobAttributeModel;
import com.sjkb.models.jobs.JobCardModel;
import com.sjkb.models.jobs.JobInvoiceRowModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.InvoiceRepository;
import com.sjkb.repositores.JobEventRepository;
import com.sjkb.repositores.JobExpenseRepository;
import com.sjkb.repositores.JobRepository;
import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobEventRepository jobEventRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    DropboxService dropboxService;

    @Autowired
    JobExpenseRepository jobExpenseRepository;

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
        int contractorCost = 0;
        int installerCost = 0;
        if (jobEvents != null && jobEvents.size() > 0) {
            for (JobEventEntity jevent : jobEvents) {
                if (jevent.getType().equals("contractor")) {
                    Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        ContactEntity contractor = optionalContact.get();
                        result.setContractor(contractor.getCompany());
                        result.setContractorId(contractor.getUid());
                    } else {
                        result.setContractor("unnamed");
                        result.setContractorId("newcontractor");
                    }
                    if (jevent.getLowEnd() > 0 && jevent.getLowEnd() == jevent.getHighEnd()) {
                        contractorCost = jevent.getLowEnd();
                    }

                }
                if (jevent.getType().equals("installer")) {
                    Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        ContactEntity contractor = optionalContact.get();
                        result.setInstaller(contractor.getCompany());
                        result.setInstallerId(contractor.getUid());
                    } else {
                        result.setInstaller("unnamed");
                        result.setInstallerId("newinstaller");
                    }
                    if (jevent.getLowEnd() > 0 && jevent.getLowEnd() == jevent.getHighEnd()) {
                        installerCost = jevent.getLowEnd();
                    }

                }
            }
            result.addCost(contractorCost);
            result.addCost(installerCost);
        }
        return result;
    }

    /**
     * @param: jobid - job folder,
     * @param: role  - this is the expense's function, ie: contractor, installer
     * 
     *               This will return the most current instance of this type of
     *               event (assumes only latest update is current, all others are
     *               historical only)
     */
    @Override
    public AssignExpenseModel getCurrentExpenseFor(String jobid, String role) {
        List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidAndTypeOrderByTimestamp(jobid, role);
        int i = jobEvents.size();
        if (i > 0) {
            return new AssignExpenseModel(jobEvents.get(i - 1));
        }
        return null;
    }

    @Override
    public String getJobHistory(JobEntity job) {
        String result = "<span>";
        List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidOrderByTimestamp(job.getJobid());
        for (JobEventEntity jobEvent : jobEvents) {
            result += jobEvent.getMessage() + "<br/>";
        }
        return result;
    }

    @Override
    public void addInvoice(InvoiceEntity invoice) {
        // TODO Auto-generated method stub

    }

    /**
     * Adds an invoice to the database, then creates a PDF that is stored in the
     * users shared project folder
     * 
     * @param AddInvoiceModel: invoice model;
     * @Param String user:
     */

    @Override
    public void addInvoice(AddInvoiceModel invoiceModel, String user) {
        ContactEntity agent = contactRepository.findByUsername(user);
        Optional<JobEntity> jobOption = jobRepository.findById(invoiceModel.getFolder());
        // must be new invoice. Updated invoice should have an existing ID
        if (jobOption.isPresent()) {
            JobEntity job = jobOption.get();
            List<UserEntity> users = userRepository.findByDbxFolder(job.getJobid());
            ContactEntity customer = contactRepository.findByUsername(users.get(0).getUsername());
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceId(UUID.randomUUID().toString().split("-")[0]);
            invoice.setContext(agent.getContext());
            invoice.setCreatorId(agent.getUid());
            invoice.setCustomerId(job.getJobid());
            invoice.setCreateDate(LocalDate.now());
            Float total = 0.0f;
            for (JobInvoiceRowModel row : invoiceModel.getRows()) {
                if (row.getName() != null && !row.getName().isEmpty()) {
                    InvoiceItemEntity item = new InvoiceItemEntity();
                    item.setInvoice(invoice.getUid());
                    item.setName(row.getName());
                    item.setDescription(row.getDesc());
                    item.setRetail(row.getCost());
                    total += item.getRetail();
                    invoice.addItem(item);
                }
            }
            job.setInvoiced(total + job.getInvoiced());
            jobRepository.save(job);
            invoiceRepository.save(invoice);
            JobEventEntity newEvent = new JobEventEntity();
            newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
            newEvent.setJobid(job.getJobid());
            newEvent.setCreatorId(job.getUser());
            newEvent.setObjid(invoice.getInvoiceId());
            newEvent.setType("invoice");
            newEvent.setLowEnd(total.intValue());
            jobEventRepository.save(newEvent);
            PdfDbxWriter pdfWriter = new PdfDbxWriter();
            UploadUploader dropbox;
            try {
                dropbox = dropboxService.getOutputFileStream(invoiceModel.getFolder(),
                        "invoice-" + invoice.getInvoiceId() + ".pdf", user);
                pdfWriter.createInvoice(invoice, customer, agent, dropbox);

            } catch (DbxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void postExpense(JobExpenseEntity expense, String user) {
        jobExpenseRepository.save(expense);
        JobEventEntity newEvent = new JobEventEntity();
        newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
        newEvent.setJobid(expense.getFolder());
        newEvent.setCreatorId(user);
        newEvent.setObjid(expense.getUid());
        newEvent.setType("expense");
        newEvent.setLowEnd(Math.round(expense.getNet()));
        jobEventRepository.save(newEvent);
    }

    
}