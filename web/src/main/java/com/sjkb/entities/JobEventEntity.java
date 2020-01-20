package com.sjkb.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    private String subType;

    private String jobid;

    private String objid;

    private String creatorId;

    private Timestamp timestamp;

    private int lowEnd;

    private int highEnd;

    private LocalDate startDate;

    private LocalDate endDate;

    private float paid;

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
            result += String.format("Posted invoice %s for %.2f", objid, paid);
            break;
        case "expense":
            if ("cabinet".equals(subType))
                result += String.format("Posted cabinet expense for $%.2f", paid);
            else
                result += String.format("Posted expense for $%.2f", paid);
            break;
        case "payment":
            result += String.format("Posted payment for %.2f", paid);
            break;
        case "note":
            result += String.format("<strong>Note:</strong>");
            break;
        case "quote":
            if (highEnd == 0) {
                result += String.format("Quoted Job at $%d", lowEnd);
            } else {
                result += String.format("Quote updated, was $%d, now $%d", highEnd, lowEnd);
            }

            break;
        default:
            if (highEnd != 0 && highEnd == lowEnd)
                result += String.format("%s updated, quote %d", type, highEnd);
            else
                result += String.format("%s updated, high %d, low %d", type, highEnd, lowEnd);
            if (paid > 0) {
                result += String.format(", paid $%s", paid);
            }
            if (startDate != null) {
                result += " for " + startDate.toString();
            }
        }
        return result;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

}