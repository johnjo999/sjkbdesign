package com.sjkb.models.jobs;

import com.sjkb.entities.InvoiceEntity;

public class OutstandingInvoiceModel {
    String invId; 
    String desc;
    float due;

    public OutstandingInvoiceModel() {}

    public OutstandingInvoiceModel(InvoiceEntity entity) {
        this.invId = entity.getInvoiceId();
        this.due = entity.getDue();
        this.desc = String.format("(%s), remaining: %.2f", entity.getInvoiceId(), due);
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getDue() {
        return due;
    }

    public void setDue(float due) {
        this.due = due;
    }

    

    
}