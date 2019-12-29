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
    private int expectedCost = 0;

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

    public int getExpectedCost() {
        return expectedCost;
    }

    public void setExpectedCost(int expectedCost) {
        this.expectedCost = expectedCost;
    }

	public void addCost(int cost) {
        this.expectedCost += cost;
	}

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    
    

}