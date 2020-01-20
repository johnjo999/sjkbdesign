package com.sjkb.models.jobs;

import java.sql.Timestamp;

import com.sjkb.entities.JobEventEntity;

/**
 * The new expense model, used as a back bean for forms where the job will only
 * have a single instance: contractor, installer and cabinet
 */

public class AssignExpenseModel {
    private String vendorId; // expense tied to vendor
    private String contId; // expense tied to contact
    private String date;
    private int lowEstimate;
    private int highEstimate;
    private int quote;
    private float paid;
    private String folder;

    public AssignExpenseModel() {
    }

    public AssignExpenseModel(String jobid) {
        this.folder = jobid;
    }

    public AssignExpenseModel(JobEventEntity job) {
        if (job.getSubType() != null) {
            // the subent determine the table holding the parent object
            switch (job.getSubType()) {
            case "cabinet":
                this.vendorId = job.getObjid();

            }
        } else
            this.vendorId = job.getObjid();
        if (job.getStartDate() != null)
            this.date = job.getStartDate().toString();
        this.lowEstimate = job.getLowEnd();
        this.highEstimate = job.getHighEnd();
        this.paid = job.getPaid();
        if (this.lowEstimate > 0 && this.lowEstimate == this.highEstimate) {
            this.quote = this.getLowEstimate();
        }
        this.folder = job.getJobid();

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
                result = Timestamp.valueOf(String.format("%s-%s-%s 00:00:00", decomp[2], decomp[0], decomp[1]));
            }
        }
        return result;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public void setLowEstimate(String bogus) {
        // ignore
    }

    public void setHighEstimate(String bogus) {
        // ignore
    }

    public void setQuote(String bogus) {
        // ignore
    }

}