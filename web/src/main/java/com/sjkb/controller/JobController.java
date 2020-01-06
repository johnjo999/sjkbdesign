package com.sjkb.controller;

import java.util.List;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.InvoiceEntity;
import com.sjkb.entities.JobEntity;
import com.sjkb.models.jobs.AssignExpenseModel;
import com.sjkb.models.jobs.InvoiceModel;
import com.sjkb.entities.JobExpenseEntity;
import com.sjkb.models.jobs.AddInvoiceModel;
import com.sjkb.models.jobs.AddNoteModel;
import com.sjkb.models.jobs.AddPaymentModel;
import com.sjkb.models.jobs.AddQuoteModel;
import com.sjkb.models.jobs.JobAttributeModel;
import com.sjkb.models.jobs.PandLModel;
import com.sjkb.repositores.InvoiceRepository;
import com.sjkb.repositores.JobExpenseInvoiceInterface;
import com.sjkb.repositores.JobExpenseRepository;
import com.sjkb.service.JobService;
import com.sjkb.service.UserContactService;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/backstage/job")
public class JobController {

    @Autowired
    JobService jobService;

    @Autowired
    JobExpenseRepository jobExpenseRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    UserContactService contactService;

    @Autowired
    BackstageController backstageController;

    TextEncryptor crypter = Encryptors.text("thesjkbkey", "AE7387");

    @RequestMapping(value = "getfolder/{folder}")
    public String getSharedFiles(ModelMap map, @PathVariable("folder") final String folder) {
        JobEntity job = jobService.getJobForFolder(folder);
        String history = jobService.getJobHistory(job);
        ContactEntity contact = contactService.getContactByUid(job.getPocId());
        JobAttributeModel jobAttributes = jobService.getAttributesFor(job);
        map.addAttribute("job", job);
        map.addAttribute("contact", contact);
        map.addAttribute("jobAttribute", jobAttributes);
        map.addAttribute("history", history);
        return "folder";
    }

    @RequestMapping(value = "get/form/{jobid}/{type}")
    public String getContractorForm(ModelMap map, @PathVariable("jobid") final String jobid,
            @PathVariable("type") final String type) {
        String form = "fragments/forms::" + type + "-form";
        AssignExpenseModel expenseModel = null;
        switch (type) {
        case "contractor":
            map.addAttribute("allCont", contactService.getContratorSelectRows());
            expenseModel = jobService.getCurrentExpenseFor(jobid, "contractor");
            break;
        case "installer":
            map.addAttribute("allCont", contactService.getInstallerSelectRows());
            expenseModel = jobService.getCurrentExpenseFor(jobid, "installer");
            break;
        case "cabinet":
            map.addAttribute("allCont", contactService.getCabinetSelectRows());
            expenseModel = jobService.getCurrentExpenseFor(jobid, "cabinet");
            break;
        case "expense":
            map.addAttribute("reps", contactService.getCompaniesWithReps(contactService.getContext()));
            JobExpenseEntity expense = new JobExpenseEntity();
            expense.setFolder(jobid);
            map.addAttribute("expense", expense);
        }
        if (expenseModel == null) {
            expenseModel = new AssignExpenseModel(jobid);
        }
        map.addAttribute("assignExpenseModel", expenseModel);
        return form;
    }

    @RequestMapping(value = "get/form/event/{jobid}/{type}")
    public String getJobEventForm(ModelMap map, @PathVariable("jobid") final String jobid,
            @PathVariable("type") final String type) {
        switch (type) {
        case "note":
            map.addAttribute("addNoteModel", new AddNoteModel(jobid));
            break;
        case "quote":
            AddQuoteModel quote = new AddQuoteModel(jobid);
            quote.setAmount(jobService.getJobForFolder(jobid).getQuote());
            map.addAttribute("addQuoteModel", quote);
            break;
        }
        return "fragments/forms::" + type + "-form";
    }

    @RequestMapping(value = "get/form/invoice/{jobid}", params = "count")
    public String getInvoiceForJob(ModelMap map, @PathVariable("jobid") final String jobid,
            @RequestParam("count") Integer count) {
        AddInvoiceModel invoiceModel = new AddInvoiceModel();
        List<JobExpenseInvoiceInterface> expenses = jobExpenseRepository.findByFolderAndNotInvoiced(jobid);
        invoiceModel.addAllExpenses(expenses);
        invoiceModel.setFolder(jobid);
        invoiceModel.createBlankRows(count);
        map.addAttribute("addInvoiceModel", invoiceModel);
        return "fragments/forms::invoice-form";

    }

    @RequestMapping(value = "get/form/payment/{jobid}")
    public String getPaymentForJob(ModelMap map, @PathVariable("jobid") final String jobid) {
        AddPaymentModel paymentModel = new AddPaymentModel();
        paymentModel.setFolder(jobid);
        List<String> methods = jobService.getPaymentMethods();
        map.addAttribute("addPaymentModel", paymentModel);
        map.addAttribute("invoiceOut", jobService.getOutstandingInvoices(jobid));
        map.addAttribute("methods", methods);
        return "fragments/forms::payment-form";

    }

    @RequestMapping(value = "set/{expense}", method = RequestMethod.POST)
    public String setJobExpense(ModelMap map, @ModelAttribute("assignExpenseModel") AssignExpenseModel expenseModel,
            @PathVariable("expense") final String expense) {
        jobService.addExpense(expense, expenseModel);
        return "redirect:/backstage/job/getfolder/" + expenseModel.getFolder();
    }

    @RequestMapping(value = "set/invoice", method = RequestMethod.POST)
    public String setJobInvoice(ModelMap map, @ModelAttribute("addInvoiceModel") AddInvoiceModel invoiceModel) {
        jobService.addInvoice(invoiceModel, contactService.getUserId());
        return "redirect:/backstage/job/getfolder/" + invoiceModel.getFolder();
    }

    @RequestMapping(value = "post/expense", method = RequestMethod.POST)
    public String setJobInvoice(ModelMap map, @ModelAttribute("JobExpenseModel") JobExpenseEntity expense) {
        jobService.postExpense(expense, contactService.getUserId());
        return "redirect:/backstage/job/getfolder/" + expense.getFolder();
    }

    @RequestMapping(value = "post/payment", method = RequestMethod.POST)
    public String setJobInvoice(ModelMap map, @ModelAttribute("addPaymentModel") AddPaymentModel payment) {
        jobService.postPayment(payment, contactService.getUserId());
        return "redirect:/backstage/job/getfolder/" + payment.getFolder();
    }

    @RequestMapping(value = "post/note", method = RequestMethod.POST)
    public String addJobNote(ModelMap map, @ModelAttribute("addNoteModel") AddNoteModel note) {
        jobService.postNote(note, contactService.getUserId());
        return "redirect:/backstage/job/getfolder/" + note.getJobid();
    }

    @RequestMapping(value = "post/quote", method = RequestMethod.POST)
    public String addJobQuote(ModelMap map, @ModelAttribute("addQuoteModel") AddQuoteModel quote) {
        jobService.postQuote(quote, contactService.getUserId());
        return "redirect:/backstage/job/getfolder/" + quote.getJobid();
    }

    @RequestMapping(value = "/get/pandl/{jobid}")
    public String getJobPandL(ModelMap map, @PathVariable("jobid") final String jobid) {
        PandLModel pandl = new PandLModel();
        JobEntity job = jobService.getJobForFolder(jobid);
        JobAttributeModel jobAttributes = jobService.getAttributesFor(job);
        pandl.setExpenses(jobService.getExpenses(jobid));
        pandl.setInvoiced(jobService.getInvoices(jobid));
        pandl.setQuote(job.getQuote());
        pandl.setCustBudget(job.getBudget());
        pandl.setCollected(jobAttributes.getCustPaid());
        pandl.setInstallerBilled(jobAttributes.getInstallerInvoiced());
        pandl.setInstallerCost(jobAttributes.getInstallerCost());
        pandl.setVendorCost(jobAttributes.getVendorPaid());
        ContactEntity contact = contactService.getContactByUid(job.getPocId());
        pandl.setJobid(jobid);
        map.addAttribute("pandl", pandl);
        map.addAttribute("contact", contact);
        return "jobs/project_pl";

    }

    @RequestMapping(value = "/get/invoice/{invoiceid}")
    public String getInvoiceById(ModelMap map, @PathVariable("invoiceid") final String invoiceid) {
        InvoiceEntity invoice = invoiceRepository.findByInvoiceId(invoiceid);
        ContactEntity contact = contactService.getContactByUid(invoice.getCustomerId());

        map.addAttribute("invoice", new InvoiceModel(invoice));
        map.addAttribute("contact", contact);
        return "/jobs/invoice";
    }

}