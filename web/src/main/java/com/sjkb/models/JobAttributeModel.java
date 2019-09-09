package com.sjkb.models;
/**
 * Attributes are created by scraping the eventlog for this job
 */
public class JobAttributeModel {
    private String contractor;
    private String contractorId;
    private String installer;
    private String installerId;

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

    

}