package com.sjkb.entities;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "expense")
@Table(name = "expense")
public class JobExpenseEntity {

    @Id
    private String uid;
    private String folder;
    private String vendorId;
    private float net;
    private float tax;
    private float shipping;
    private float paid;
    private float multiplier;
    private float invoice;
    private LocalDate dateNew;
    private boolean invoiced;
    
    public JobExpenseEntity() {
        uid = UUID.randomUUID().toString();
        dateNew = LocalDate.now();
    }

    public float getNet() {
        return net;
    }

    public void setNet(float net) {
        this.net = net;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public float getInvoice() {
        return invoice;
    }

    public void setInvoice(float invoice) {
        this.invoice = invoice;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDate getDateNew() {
        return dateNew;
    }

    public void setDateNew(LocalDate dateNew) {
        this.dateNew = dateNew;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    
    

    
}