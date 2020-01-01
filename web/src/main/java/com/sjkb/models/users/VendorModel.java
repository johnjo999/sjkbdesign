package com.sjkb.models.users;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class VendorModel {
    private String name;
    private Float multiplier;
    private String accountId;
    private String pocId;
    private String url;
    private String postNextAction;
    private String login;
    private String pwd;
    private boolean cabSupplier;

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

    public String getPwdEncrypt() {
        TextEncryptor crypter = Encryptors.text((CharSequence)"thesjkbkey", "AE7387");
        return crypter.encrypt(pwd);
	}
    
    public String getPwdDecrypt() {
        TextEncryptor crypter = Encryptors.text("thesjkbkey", "AE7387");
        return crypter.decrypt(pwd);
    }

    public boolean isCabSupplier() {
        return cabSupplier;
    }

    public void setCabSupplier(boolean cabSupplier) {
        this.cabSupplier = cabSupplier;
    }

    
}