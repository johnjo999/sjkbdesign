package com.sjkb.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.users.VendorModel;

@Entity(name = "vendor")
@Table(name = "vendor")
public class VendorEntity {
    @Id
    private String uid = UUID.randomUUID().toString();
    private String name;
    private String repId;       
    private String context;
    private String accountId;
    private String url;
    private Float multiplier;
    private String login;
    private String pwd;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Float multiplier) {
        this.multiplier = multiplier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    

    public void parseModel(VendorModel model) {
        this.accountId = model.getAccountId();
        this.multiplier = model.getMultiplier();
        this.url = model.getUrl();
        this.name = model.getName();
        this.repId = model.getPocId();
        this.login = model.getLogin();
        this.pwd = model.getPwdEncrypt();
    }

    public VendorModel getModel() {
        VendorModel result = new VendorModel();
        result.setAccountId(this.accountId);
        result.setMultiplier(this.multiplier);
        result.setUrl(this.url);
        result.setName(this.name);
        result.setPocId(this.repId);
        result.setLogin(this.login);
        result.setPwd(this.pwd);
        result.setPwd(result.getPwdDecrypt());
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
}