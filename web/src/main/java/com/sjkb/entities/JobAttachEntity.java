package com.sjkb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.jobs.AddNoteModel;

@Entity(name = "jobAttachEvent")
@Table(name = "job_notes")
public class JobAttachEntity {

    // metadata held in job event entity with identical uid
    @Id
    private String uid;

    private String note;

    private String jobid;

    // binary blob - note most large blob should be dropbox files
    private boolean bin;

    // customer viewable
    private boolean pub;

    public JobAttachEntity() {}

    public JobAttachEntity(AddNoteModel model) {
        this.note = model.getNote();
        this.jobid = model.getJobid();
        this.pub = model.isPub();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(boolean bin) {
        this.bin = bin;
    }

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    

    
    
}