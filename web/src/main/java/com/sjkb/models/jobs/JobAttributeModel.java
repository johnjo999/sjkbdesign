package com.sjkb.models.jobs;

import java.time.LocalDate;

/**
 * Attributes are created by scraping the eventlog for this job
 */
public class JobAttributeModel {
    private String contractor;
    private String contractorId;
    private String installer;
    private String installerId;
    private String designer;
    private float margin;
    private float invoicedRetail;
    private float invoicedCost;
    private float custPaid;
    private float vendorCost;
    private int cabinetQuote;
    private float cabinetInvoiced;
    private float cabinetCost;
    private int installerCost;
    private int installerInvoiced;
    private int contractorCost;
    private int contractorInvoiced;
    private int budgetLow;
    private int budgetHigh;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getInstaller() {
        return installer;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    public String getContractorId() {
        return contractorId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
    }

    public String getInstallerId() {
        return installerId;
    }

    public void setInstallerId(String installerId) {
        this.installerId = installerId;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public float getInvoicedRetail() {
        return invoicedRetail;
    }

    public void setInvoicedRetail(float invoicedRetail) {
        this.invoicedRetail = invoicedRetail;
    }

    public float getInvoicedCost() {
        return invoicedCost;
    }

    public void setInvoicedCost(float invoicedCost) {
        this.invoicedCost = invoicedCost;
    }

    public float getCustPaid() {
        return custPaid;
    }

    public void setCustPaid(float custPaid) {
        this.custPaid = custPaid;
    }

    public float getVendorCost() {
        return vendorCost;
    }

    public void setVendorCost(float vendorPaid) {
        this.vendorCost = vendorPaid;
    }

    public int getInstallerCost() {
        return installerCost;
    }

    public void setInstallerCost(int installerCost) {
        this.installerCost = installerCost;
    }

    public int getInstallerInvoiced() {
        return installerInvoiced;
    }

    public void setInstallerInvoiced(int installerInvoiced) {
        this.installerInvoiced = installerInvoiced;
    }

    public int getContractorCost() {
        return contractorCost;
    }

    public void setContractorCost(int contractorCost) {
        this.contractorCost = contractorCost;
    }

    public int getContractorInvoiced() {
        return contractorInvoiced;
    }

    public void setContractorInvoiced(int contractorInvoiced) {
        this.contractorInvoiced = contractorInvoiced;
    }

	public Integer getExpectedCost() {
		return Math.round(installerCost + vendorCost + cabinetQuote);
	}

	public void addVendorCost(float cost) {
        vendorCost += cost;
	}

	public void addCustPayment(float paid) {
        custPaid += paid;
    }

    public float getCabinetInvoiced() {
        return cabinetInvoiced;
    }

    public void setCabinetInvoiced(float cabinetInvoiced) {
        this.cabinetInvoiced = cabinetInvoiced;
    }

    public void addCabInvoice(int cabInvoiced) {
        this.cabinetInvoiced += cabInvoiced;
    }

    public void addInvoicedRetial(float cost) {
        this.invoicedRetail += cost;
    }

    public void addInvoicedCost(float cost) {
        this.invoicedCost += cost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getBudgetLow() {
        return budgetLow;
    }

    public void setBudgetLow(int budgetLow) {
        this.budgetLow = budgetLow;
    }

    public int getBudgetHigh() {
        return budgetHigh;
    }

    public void setBudgetHigh(int budgetHigh) {
        this.budgetHigh = budgetHigh;
    }

    public int getCabinetQuote() {
        return cabinetQuote;
    }

    public void setCabinetQuote(int cabinetQuote) {
        this.cabinetQuote = cabinetQuote;
    }

	public float getCabinetCost() {
		return cabinetCost;
    }

    public void setCabinetCost(float cabinetCost) {
        this.cabinetCost = cabinetCost;
    }
    
    

}