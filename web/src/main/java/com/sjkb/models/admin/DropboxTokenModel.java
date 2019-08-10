package com.sjkb.models.admin;

import com.sjkb.entities.DropboxTokenEntity;

public class DropboxTokenModel {

    private String user;
    private String token;

    public DropboxTokenModel(DropboxTokenEntity token) {
        this.user = token.getUser();
        this.token = token.getToken();
	}

	public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}
