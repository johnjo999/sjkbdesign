package com.sjkb.models.jobs;

public class AddQuoteModel extends AddNoteModel {
    private int amount;

    public AddQuoteModel() {}

    public AddQuoteModel(String jobid) {
        super(jobid);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    
}