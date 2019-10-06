package com.sjkb.models.users;

public class VendorModel {
    private String name;
    private Float multiplier;
    private String accountId;
    private String pocId;
    private String url;
    private String postNextAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Float multiplier) {
        this.multiplier = multiplier;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPocId() {
        return pocId;
    }

    public void setPocId(String pocId) {
        this.pocId = pocId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostNextAction() {
        return postNextAction;
    }

    public void setPostNextAction(String postNextAction) {
        this.postNextAction = postNextAction;
    }

    
}