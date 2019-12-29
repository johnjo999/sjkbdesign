package com.sjkb.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "jobEvent")
@Table(name = "job_event")
public class JobEventEntity {

    @Id
    private String uid;

    private String type;

    private String jobid;

    private String objid;

    private String creatorId;

    private Timestamp timestamp;

    private int lowEnd;

    private int highEnd;

    private Timestamp scheduled;

    @Transient
    private String username;

    public JobEventEntity() {
        uid = UUID.randomUUID().toString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(final String objid) {
        this.objid = objid;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(final String creatorId) {
        this.creatorId = creatorId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(final String jobid) {
        this.jobid = jobid;
    }

    public int getLowEnd() {
        return lowEnd;
    }

    public void setLowEnd(final int lowEnd) {
        this.lowEnd = lowEnd;
    }

    public int getHighEnd() {
        return highEnd;
    }

    public void setHighEnd(final int highEnd) {
        this.highEnd = highEnd;
    }

    

    public String getMessage() {
        String date = "";
        if (timestamp != null)
            date = timestamp.toString().split(" ")[0];
        String result = String.format("%s (%s): ", date, this.username);
        switch (type) {
        case "invoice":
            result += String.format("Posted invoice %s for %d", objid, lowEnd);
            break;
        case "expense":
            result += String.format("Posted expense for %d", lowEnd);
            break;
        case "payment":
            result += String.format("Posted payment for %d", lowEnd);
            break;
        default:
            if (highEnd != 0 && highEnd == lowEnd)
                result += String.format("%s updated, quote %d", type, highEnd);
            else
                result += String.format("%s updated, high %d, low %d", type, highEnd, lowEnd);
            if (scheduled != null) {
                result += " for " + scheduled.toString().split(" ")[0];
            }
        }
        return result;

    }

    public Timestamp getScheduled() {
        return scheduled;
    }

    public void setScheduled(final Timestamp scheduled) {
        this.scheduled = scheduled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}