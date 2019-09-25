package com.sjkb.models;

public class UserDelModel {
    private String uid;
    private boolean delJobs;
    private boolean delDropbox;
    private String type;

    
    public boolean isDelJobs() {
        return delJobs;
    }

    public void setDelJobs(boolean delJobs) {
        this.delJobs = delJobs;
    }

    public boolean isDelDropbox() {
        return delDropbox;
    }

    public void setDelDropbox(boolean delDropbox) {
        this.delDropbox = delDropbox;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}