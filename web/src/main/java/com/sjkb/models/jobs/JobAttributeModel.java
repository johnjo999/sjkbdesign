package com.sjkb.models.jobs;

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
    private float vendorPaid;
    private float cabinetCost;
    private float cabinetInvoiced;
    private int installerCost;
    private int installerInvoiced;
    private int contractorCost;
    private int contractorInvoiced;

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

    public float getVendorPaid() {
        return vendorPaid;
    }

    public void setVendorPaid(float vendorPaid) {
        this.vendorPaid = vendorPaid;
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
		return Math.round(contractorCost + installerCost + vendorPaid);
	}

	public void addVendorCost(int lowEnd) {
        vendorPaid += lowEnd;
	}

	public void addCustPayment(int paid) {
        custPaid += paid;
    }

    public float getCabinetCost() {
        return cabinetCost;
    }

    public void setCabinetCost(float cabinetCost) {
        this.cabinetCost = cabinetCost;
    }

    public float getCabinetInvoiced() {
        return cabinetInvoiced;
    }

    public void setCabinetInvoiced(float cabinetInvoiced) {
        this.cabinetInvoiced = cabinetInvoiced;
    }
    
    public void addCabinetCost(int cabCost) {
        this.cabinetCost += cabCost;
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
   

}