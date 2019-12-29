package com.sjkb.models.jobs;

import com.sjkb.entities.JobExpenseEntity;

public class PandLExpenseModel {

    private String vendor;
    private String gross;
    private String tax;
    private String net;

    public void setEntityAmounts(JobExpenseEntity entity) {
        this.tax = String.format("%.2f", entity.getTax());
        this.gross = String.format("%.2f", entity.getPaid());
        this.net = String.format("%.2f", entity.getPaid() - entity.getTax());
    }

    public String getVendor() {
        if (vendor == null)
            return "retail";
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    
}