package com.sjkb.models.jobs;

import com.sjkb.repositores.JobExpenseInvoiceInterface;

public class JobInvoiceRowModel {
    private String name;
    private String desc;
    private Float cost;
    private String expenseUid;  // mark expense as invoices (where available)

    public JobInvoiceRowModel() {}
    
    public JobInvoiceRowModel(JobExpenseInvoiceInterface expense) {
        name = expense.getCompanyName();
        cost = expense.getCost();
        expenseUid = expense.getUid();
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpenseUid() {
        return expenseUid;
    }

    public void setExpenseUid(String expenseUid) {
        this.expenseUid = expenseUid;
    }

    
    
    
}