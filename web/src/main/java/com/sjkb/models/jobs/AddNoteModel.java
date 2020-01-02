package com.sjkb.models.jobs;

public class AddNoteModel {
    private String jobid;
    private String note;
    private boolean pub;

    public AddNoteModel() {}

    public AddNoteModel(String jobid) {
        this.jobid = jobid;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    

    
}