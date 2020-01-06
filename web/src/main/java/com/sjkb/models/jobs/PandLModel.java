package com.sjkb.models.jobs;

import java.util.List;
import com.sjkb.entities.JobPaymentEntity;

public class PandLModel {
    String jobid;
    List<PandLExpenseModel> expenses;
    List<PandLInvoiceModel> invoiced;
    List<JobPaymentEntity> account;
    private int custBudget;
    private int contractorCost;
    private int installerCost;
    private int installerBilled;
    private int quote;
    private float collected;
    private float vendorCost;
    private float custInvoiced;

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

    public int getCustBudget() {
        return custBudget;
    }

    public void setCustBudget(int custBudget) {
        this.custBudget = custBudget;
    }

    public int getContractorCost() {
        return contractorCost;
    }

    public void setContractorCost(int contractorCost) {
        this.contractorCost = contractorCost;
    }

    public int getQuote() {
        return quote;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }

    public int getInvoiceBilled() {
        return 0;
    }

    public int getInvoiceCost() {
        return 0;
    }

    public int getCustomerPaid() {
        return 0;
    }

    public int getBalance() {
        return 0;
    }

    public int getSjkbBudget() {
        return 0;
    }

    public int getInstallerCost() {
        return installerCost;
    }

    public void setInstallerCost(int installerCost) {
        this.installerCost = installerCost;
    }

    public int getInstallerBilled() {
        return installerBilled;
    }

    public void setInstallerBilled(int installerBilled) {
        this.installerBilled = installerBilled;
    }

    public int getSjkbPaid() {
        return Math.round(vendorCost + installerCost);
    }

    public float getCollected() {
        return collected;
    }

    public void setCollected(float collected) {
        this.collected = collected;
    }

    public int getExposure() {
        return 0;
    }

    public int getProjectedProfit() {
        return 0;
    }

    public float getVendorCost() {
        return vendorCost;
    }

    public void setVendorCost(float vendorCost) {
        this.vendorCost = vendorCost;
    }

    public float getCustInvoiced() {
        return custInvoiced;
    }

    public void setCustInvoiced(float custInvoiced) {
        this.custInvoiced = custInvoiced;
    }
    
}