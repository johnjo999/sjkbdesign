package com.sjkb.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "invoiceItem")
@Table(name = "invoice_item")
public class InvoiceItemEntity {

    @Id
    private String uid = UUID.randomUUID().toString();

    private Long invoice;
    private String name;
    private String description;
    private long catalogId;
    private float retail;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(long catalogId) {
        this.catalogId = catalogId;
    }

    public float getRetail() {
        return retail;
    }

    public void setRetail(Float float1) {
        this.retail = float1;
    }

    public Long getInvoice() {
        return invoice;
    }

    public void setInvoice(Long invoice) {
        this.invoice = invoice;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRetail(float retail) {
        this.retail = retail;
    }

    

}