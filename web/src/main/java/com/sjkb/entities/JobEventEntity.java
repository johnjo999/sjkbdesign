package com.sjkb.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public JobEventEntity() {
        uid = UUID.randomUUID().toString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public int getLowEnd() {
        return lowEnd;
    }

    public void setLowEnd(int lowEnd) {
        this.lowEnd = lowEnd;
    }

    public int getHighEnd() {
        return highEnd;
    }

    public void setHighEnd(int highEnd) {
        this.highEnd = highEnd;
    }

    public String getMessage() {
        String date = "";
        if (timestamp != null)
            date = timestamp.toString().split(" ")[0];
        String result = String.format("%s (%s): ", date, this.creatorId);
        if (highEnd != 0 && highEnd == lowEnd)
            result += String.format("%s updated, quote %d", type, highEnd);
        else
            result += String.format("%s updated, high %d, low %d", type, highEnd, lowEnd);
        if (scheduled != null) {
            result += " for "+scheduled.toString().split(" ")[0];
        }
        return result;
    }

    public Timestamp getScheduled() {
        return scheduled;
    }

    public void setScheduled(Timestamp scheduled) {
        this.scheduled = scheduled;
    }

}