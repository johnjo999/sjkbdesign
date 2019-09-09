package com.sjkb.models;

import java.sql.Timestamp;

import com.sjkb.entities.JobEventEntity;

public class AssignExpenseModel {
    private String contId;
    private String date;
    private int lowEstimate;
    private int highEstimate;
    private int quote;
    private String folder;

    public AssignExpenseModel() {}

    public AssignExpenseModel(JobEventEntity job) {
        this.contId = job.getObjid();
        this.date = job.getTimestamp().toString().split(" ")[0];
        this.lowEstimate = job.getLowEnd();
        this.highEstimate = job.getHighEnd();
        if (this.lowEstimate > 0 && this.lowEstimate == this.highEstimate) {
            this.quote = this.getLowEstimate();
        }
        this.folder = job.getJobid();

    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

  
    public int getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(int lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(int highEstimate) {
        this.highEstimate = highEstimate;
    }

    public int getQuote() {
        return quote;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

	public Timestamp getTimestamp() {
        Timestamp result = null;
		if (date != null && date.length() > 2) {
            String[] decomp = date.split("/");
            if (decomp.length == 3) {
                result = Timestamp.valueOf(String.format("%s-%s-%s 00:00:00",decomp[2], decomp[0], decomp[1]));
            }
        }
        return result;
	}


    
}