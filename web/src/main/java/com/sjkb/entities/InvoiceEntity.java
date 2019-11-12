package com.sjkb.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "invoice")
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    private long uid = System.currentTimeMillis();

    private String invoiceId;
    private String context;
    private String customerId;
    private String creatorId;
    private LocalDate createDate;
    private LocalDate userViewDate;
    private boolean signed;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice")
    private List<InvoiceItemEntity> items = new ArrayList<>();

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getContext() {
        return context;
    }

    
    public void setContext(String context) {
        this.context = context;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUserViewedDate() {
        return userViewDate;
    }

    public void setUserViewedDate(LocalDate userViewDate) {
        this.userViewDate = userViewDate;
    }

    public List<InvoiceItemEntity> getItems() {
        return items;
    }

    public void addItem(InvoiceItemEntity item) {
        item.setInvoice(this.uid);
        this.items.add(item);
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    
    

}