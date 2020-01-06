package com.sjkb.models.jobs;

import java.time.LocalDate;
import java.util.List;

import com.sjkb.entities.InvoiceEntity;
import com.sjkb.entities.InvoiceItemEntity;

public class InvoiceModel {
    private String jobid;
    private String invoiceId;
    private List<InvoiceItemEntity> items;
    private LocalDate date;
    private float paid;

    public InvoiceModel() {
    }

    public InvoiceModel(InvoiceEntity entity) {
        this.jobid = entity.getJobId();
        this.invoiceId = entity.getInvoiceId();
        this.items = entity.getItems();
        this.date = entity.getCreateDate();
        this.paid = entity.getPaid();
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<InvoiceItemEntity> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemEntity> items) {
        this.items = items;
    }

    public String getTotal() {
        float total = 0.0f;
        for (InvoiceItemEntity item : items) {
            total += item.getRetail();
        }
        return String.format("$%.2f", total);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public String getDue() {
        float total = 0.0f;
        for (InvoiceItemEntity item : items) {
            total += item.getRetail();
        }
        return String.format("$%.2f", total - paid);
    }

}