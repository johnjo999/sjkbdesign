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
import com.sjkb.entities.JobPaymentEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.models.AssignExpenseModel;
import com.sjkb.models.jobs.AddInvoiceModel;
import com.sjkb.models.jobs.AddPaymentModel;
import com.sjkb.models.jobs.JobAttributeModel;
import com.sjkb.models.jobs.JobCardModel;
import com.sjkb.models.jobs.JobInvoiceRowModel;
import com.sjkb.models.jobs.PandLExpenseModel;
import com.sjkb.models.jobs.PandLInvoiceModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.InvoiceRepository;
import com.sjkb.repositores.JobEventRepository;
import com.sjkb.repositores.JobExpenseRepository;
import com.sjkb.repositores.JobPaymentRepository;
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

    @Autowired
    JobPaymentRepository jobPaymentRepository;

    @Autowired
    UserContactService userService;

    @Override
    public void createJob(final String jobid, final String designer, final String pocId) {
        final JobEntity job = new JobEntity();
        job.setJobid(jobid);
        job.setPocId(pocId);
        job.setCreateDate(LocalDate.now());
        job.setActivityDate(new Timestamp(System.currentTimeMillis()));
        job.setUser(designer);
        job.setBudget(0);
        job.setQuote(0);
        job.setContext("na");
        job.setState("new");
        jobRepository.save(job);
    }

    @Override
    public List<JobCardModel> getAllJobsForUser(final String empid, String status) {
        final List<JobCardModel> result = new ArrayList<>();
        final List<JobEntity> userAlljobs = jobRepository.findAllByUser(empid);
        final List<JobEntity> userjobs = new ArrayList<>();
        for (final JobEntity thisjob : userAlljobs) {
            switch (status) {
            case "active":
                if (thisjob.getCompleteDate() == null)
                    userjobs.add(thisjob);
                break;
            case "closed":
                if (thisjob.getCompleteDate() != null)
                    userjobs.add(thisjob);
                break;
            case "all":
                userjobs.add(thisjob);
                break;
            default:

            }
        }
        for (final JobEntity job : userjobs) {
            final JobCardModel card = new JobCardModel();
            final Optional<ContactEntity> contactOpt = contactRepository.findById(job.getPocId());
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
    public JobEntity getJobForFolder(final String folder) {
        final Optional<JobEntity> job = jobRepository.findById(folder);
        if (job.isPresent())
            return job.get();
        return new JobEntity();
    }

    @Override
    public void addExpense(final String expense, final AssignExpenseModel expenseModel) {
        final Optional<JobEntity> jobOptional = jobRepository.findById(expenseModel.getFolder());
        JobEntity job = null;
        if (jobOptional.isPresent()) {
            job = jobOptional.get();
        } else
            return;
        if (expenseModel.getLowEstimate() > expenseModel.getHighEstimate()) {
            final int le = expenseModel.getLowEstimate();
            expenseModel.setLowEstimate(expenseModel.getHighEstimate());
            expenseModel.setHighEstimate(le);
        }
        switch (expense) {
        case "contractor":
        case "installer":
            final JobEventEntity newEvent = new JobEventEntity();
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
    public JobAttributeModel getAttributesFor(final JobEntity job) {
        final JobAttributeModel result = new JobAttributeModel();
        final List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidOrderByTimestamp(job.getJobid());
        int contractorCost = 0;
        int installerCost = 0;
        Optional<UserEntity> designerOptional = userRepository.findByUsername(job.getUser());
        if (designerOptional.isPresent())
            result.setDesigner(designerOptional.get().getUsernameClear());
        if (jobEvents != null && jobEvents.size() > 0) {
            for (final JobEventEntity jevent : jobEvents) {
                if (jevent.getType().equals("contractor")) {
                    final Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        final ContactEntity contractor = optionalContact.get();
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
                    final Optional<ContactEntity> optionalContact = contactRepository.findById(jevent.getObjid());
                    if (optionalContact.isPresent()) {
                        final ContactEntity contractor = optionalContact.get();
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
    public AssignExpenseModel getCurrentExpenseFor(final String jobid, final String role) {
        final List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidAndTypeOrderByTimestamp(jobid, role);
        final int i = jobEvents.size();
        if (i > 0) {
            return new AssignExpenseModel(jobEvents.get(i - 1));
        }
        return null;
    }

    @Override
    public String getJobHistory(final JobEntity job) {
        String result = "<span>";
        final List<JobEventEntity> jobEvents = jobEventRepository.findAllByJobidOrderByTimestamp(job.getJobid());
        for (final JobEventEntity jobEvent : jobEvents) {
            jobEvent.setUsername(userService.getUsernameFor(jobEvent.getCreatorId()));
            result += jobEvent.getMessage() + "<br/>";
        }
        return result;
    }

    /**
     * Adds an invoice to the database, then creates a PDF that is stored in the
     * users shared project folder
     * 
     * @param AddInvoiceModel: invoice model;
     * @Param String user:
     */

    @Override
    public void addInvoice(final AddInvoiceModel invoiceModel, final String user) {
        final ContactEntity agent = contactRepository.findByUsername(user);
        final Optional<JobEntity> jobOption = jobRepository.findById(invoiceModel.getFolder());
        // must be new invoice. Updated invoice should have an existing ID
        if (jobOption.isPresent()) {
            final JobEntity job = jobOption.get();
            final List<UserEntity> users = userRepository.findByJobid(job.getJobid());
            final ContactEntity customer = contactRepository.findByUsername(users.get(0).getUsername());
            final InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceId(UUID.randomUUID().toString().split("-")[0]);
            invoice.setContext(agent.getContext());
            invoice.setCreatorId(agent.getUid());
            invoice.setCustomerId(job.getJobid());
            invoice.setCreateDate(LocalDate.now());
            Float total = 0.0f;
            for (final JobInvoiceRowModel row : invoiceModel.getRows()) {
                if (row.getName() != null && !row.getName().isEmpty()) {
                    final InvoiceItemEntity item = new InvoiceItemEntity();
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
            final JobEventEntity newEvent = new JobEventEntity();
            newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
            newEvent.setJobid(job.getJobid());
            newEvent.setCreatorId(job.getUser());
            newEvent.setObjid(invoice.getInvoiceId());
            newEvent.setType("invoice");
            newEvent.setLowEnd(total.intValue());
            jobEventRepository.save(newEvent);
            final PdfDbxWriter pdfWriter = new PdfDbxWriter();
            UploadUploader dropbox;
            try {
                dropbox = dropboxService.getOutputFileStream(invoiceModel.getFolder(),
                        "invoice-" + invoice.getInvoiceId() + ".pdf", user);
                pdfWriter.createInvoice(invoice, customer, agent, dropbox);

            } catch (final DbxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void postExpense(final JobExpenseEntity expense, final String user) {
        final Optional<JobEntity> jobOption = jobRepository.findById(expense.getFolder());
        if (jobOption.isPresent()) {
            jobExpenseRepository.save(expense);
            final JobEventEntity newEvent = new JobEventEntity();
            newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
            newEvent.setJobid(expense.getFolder());
            newEvent.setCreatorId(user);
            newEvent.setObjid(expense.getUid());
            newEvent.setType("expense");
            newEvent.setLowEnd(Math.round(expense.getNet()));
            jobEventRepository.save(newEvent);
            // JobEntity job = jobOption.get();
            // job.setReceived(job.getExpensed() + expense.get );
        }
    }

    @Override
    public List<String> getPaymentMethods() {
        final List<String> methods = new ArrayList<>();

        methods.add("Check");
        methods.add("Credit Card");
        methods.add("Cash");
        methods.add("Financed");

        return methods;
    }

    @Override
    public void postPayment(AddPaymentModel paymentModel, String user) {
        final Optional<JobEntity> jobOption = jobRepository.findById(paymentModel.getFolder());
        if (jobOption.isPresent()) {
            JobPaymentEntity payment = new JobPaymentEntity();
            payment.createFromModel(paymentModel);
            jobPaymentRepository.save(payment);
            final JobEventEntity newEvent = new JobEventEntity();
            newEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));
            newEvent.setJobid(payment.getFolder());
            newEvent.setCreatorId(user);
            newEvent.setObjid(payment.getUid());
            newEvent.setType("payment");
            newEvent.setLowEnd(Math.round(payment.getPaid()));
            jobEventRepository.save(newEvent);
            JobEntity job = jobOption.get();
            job.setReceived(job.getReceived() + paymentModel.getPaid());
            jobRepository.save(job);
        }

    }

    @Override
    public List<PandLExpenseModel> getExpenses(String jobid) {
        List<PandLExpenseModel> result = new ArrayList<>();
        List<JobExpenseEntity> entities = jobExpenseRepository.findByFolder(jobid);
        if (entities != null) {
            for (JobExpenseEntity expense : entities) {
                PandLExpenseModel pandl = new PandLExpenseModel();
                pandl.setEntityAmounts(expense);
                pandl.setVendor(contactRepository.getCompanyForId(expense.getCompanyContactId()));
                result.add(pandl);
            }
        }
        return result;
    }

    @Override
    public List<PandLInvoiceModel> getInvoices(String jobid) {
        List<PandLInvoiceModel> result = new ArrayList<>();
        List<InvoiceEntity> entities = invoiceRepository.findByCustomerId(jobid);
        for (InvoiceEntity ent : entities) {
            PandLInvoiceModel pandl = new PandLInvoiceModel();
            List<InvoiceItemEntity> items = ent.getItems();
            if (items != null) {
                for (InvoiceItemEntity item : items) {
                    pandl.addAmount(item.getRetail());
                }
            }
            pandl.setDateSubmitted(ent.getCreateDate());
            pandl.setDesc(ent.getDescription());
            pandl.setDateViewed(ent.getUserViewDate());
            pandl.setInvoiceId(ent.getInvoiceId());
            result.add(pandl);
        }
        return result;
    }

}