package com.sjkb.models.jobs;

public class AddPaymentModel {

    float paid;
    float tax;
    float processing;
    String invoice;
    String method;
    String folder;

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getProcessing() {
        return processing;
    }

    public void setProcessing(float processing) {
        this.processing = processing;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    


}