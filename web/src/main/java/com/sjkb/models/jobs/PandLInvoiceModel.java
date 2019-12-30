package com.sjkb.models.jobs;

import java.time.LocalDate;

public class PandLInvoiceModel {

    private LocalDate dateSubmitted;
    private LocalDate dateViewed;
    private String invoiceId;
    private String desc;
    private float amountDue = 0.0f;
    private float amountPaid = 0.0f;
    private Long uid;

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public LocalDate getDateViewed() {
        return dateViewed;
    }

    public void setDateViewed(LocalDate dateViewed) {
        this.dateViewed = dateViewed;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void addAmount(float amount) {
        amountDue += amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    

}