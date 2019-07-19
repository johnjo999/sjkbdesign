package com.sjkb.models;

import java.util.UUID;

public class CardModel {
    private String id;
    private String custId;
    private String title;
    private String label;
    private String cabline;
    private String counterTop;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CardModel() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCabline() {
        return cabline;
    }

    public void setCabline(String cabline) {
        this.cabline = cabline;
    }

    public String getCounterTop() {
        return counterTop;
    }

    public void setCounterTop(String counterTop) {
        this.counterTop = counterTop;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    
    
}