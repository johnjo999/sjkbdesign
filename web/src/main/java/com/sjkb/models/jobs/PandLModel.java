package com.sjkb.models.jobs;

import java.util.List;

import com.sjkb.entities.InvoiceEntity;
import com.sjkb.entities.JobPaymentEntity;

public class PandLModel {
    String jobid;
    List<PandLExpenseModel> expenses;
    List<InvoiceEntity> invoiced;
    List<JobPaymentEntity> account;

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public List<InvoiceEntity> getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(List<InvoiceEntity> invoiced) {
        this.invoiced = invoiced;
    }

    public List<JobPaymentEntity> getAccount() {
        return account;
    }

    public void setAccount(List<JobPaymentEntity> account) {
        this.account = account;
    }

    public List<PandLExpenseModel> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<PandLExpenseModel> expenses) {
        this.expenses = expenses;
    }

    
    
}