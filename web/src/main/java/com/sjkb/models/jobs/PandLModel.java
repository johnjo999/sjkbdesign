package com.sjkb.models.jobs;

import java.util.List;
import com.sjkb.entities.JobPaymentEntity;

public class PandLModel {
    String jobid;
    List<PandLExpenseModel> expenses;
    List<PandLInvoiceModel> invoiced;
    List<JobPaymentEntity> account;

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
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

    public List<PandLInvoiceModel> getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(List<PandLInvoiceModel> invoiced) {
        this.invoiced = invoiced;
    }
    
}