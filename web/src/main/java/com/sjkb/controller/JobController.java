package com.sjkb.controller;

import java.util.ArrayList;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.JobEntity;
import com.sjkb.models.AssignExpenseModel;
import com.sjkb.models.jobs.AddInvoiceModel;
import com.sjkb.models.jobs.JobAttributeModel;
import com.sjkb.models.category.ContractorSelectRow;
import com.sjkb.service.JobService;
import com.sjkb.service.UserContactService;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    UserContactService contactService;

    private String getUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        return uname;
    }

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
    public String getContractorForm(ModelMap map, 
            @PathVariable("jobid") final String jobid, @PathVariable("type") final String type) {
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
            map.addAttribute("allCont", new ArrayList<ContractorSelectRow>());
        }
        if (expenseModel == null) {
            expenseModel = new AssignExpenseModel();
        }
        map.addAttribute("assignExpenseModel", expenseModel);
        return form;
    }

    @RequestMapping(value = "get/form/invoice/{jobid}", params = "count")
    public String getInvoiceForJob(ModelMap map, @PathVariable("jobid") final String jobid, @RequestParam("count") Integer count) {
        AddInvoiceModel invoiceModel = new AddInvoiceModel();
        invoiceModel.setFolder(jobid);
        invoiceModel.createBlankRows(count);
        map.addAttribute("addInvoiceModel", invoiceModel);
        return "fragments/forms::invoice-form";

    }

    @RequestMapping(value = "set/{expense}", method = RequestMethod.POST)
    public String setJobExpense(ModelMap map, @ModelAttribute("assignExpenseModel") AssignExpenseModel expenseModel,
            @PathVariable("expense") final String expense) {
        jobService.addExpense(expense, expenseModel);
        return "redirect:/backstage/job/getfolder/" + expenseModel.getFolder();
    }

    @RequestMapping(value = "set/invoice", method = RequestMethod.POST)
    public String setJobInvoice(ModelMap map, @ModelAttribute("addInvoiceModel") AddInvoiceModel invoiceModel) {
        jobService.addInvoice(invoiceModel, getUser());
        return "redirect:/backstage/job/getfolder/" + invoiceModel.getFolder();
    }

}