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
    private float installerPaid;
    private int installerBilled;
    private float cabinetBilled;
    private int quote;
    private float cabinetCost;
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
        return custBudget - contractorCost;
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
        return Math.round(vendorCost + installerCost + cabinetCost);
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

    public float getInstallerPaid() {
        return installerPaid;
    }

    public void setInstallerPaid(float installerPaid) {
        this.installerPaid = installerPaid;
    }

    public float getCabinetCost() {
        return cabinetCost;
    }

    public void setCabinetCost(float cabinetCost) {
        this.cabinetCost = cabinetCost;
    }

    public float getCabinetBilled() {
        return cabinetBilled;
    }

    public void setCabinetBilled(float cabinetBilled) {
        this.cabinetBilled = cabinetBilled;
    }

	public void setJobAtributes(JobAttributeModel jobAttributes) {
        this.setCollected(jobAttributes.getCustPaid());
        this.setInstallerBilled(jobAttributes.getInstallerInvoiced());
        this.setInstallerCost(jobAttributes.getInstallerCost());
        this.setVendorCost(jobAttributes.getVendorCost());
        this.setCabinetCost(jobAttributes.getCabinetCost());
        this.setCabinetBilled(jobAttributes.getCabinetInvoiced());
        this.setContractorCost(jobAttributes.getContractorCost());
	}

    
}