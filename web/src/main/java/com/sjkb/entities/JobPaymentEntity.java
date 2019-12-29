package com.sjkb.entities;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.jobs.AddPaymentModel;

@Entity(name = "payment")
@Table(name = "payment")
public class JobPaymentEntity {

    @Id
    private String uid;
    private float paid;
    private float tax;
    private float fee;
    private String method;
    private String folder;
    private LocalDate dateNew;

    public JobPaymentEntity() {
        uid = UUID.randomUUID().toString();
        dateNew = LocalDate.now();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public LocalDate getDateNew() {
        return dateNew;
    }

    public void setDateNew(LocalDate dateNew) {
        this.dateNew = dateNew;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public JobPaymentEntity createFromModel(AddPaymentModel paymentModel) {
        setPaid(paymentModel.getPaid());
        setDateNew(LocalDate.now());
        setFee(paymentModel.getProcessing());
        setTax(paymentModel.getTax());
        setFolder(paymentModel.getFolder());
        setMethod(paymentModel.getMethod());

        return this;
    }
    

}