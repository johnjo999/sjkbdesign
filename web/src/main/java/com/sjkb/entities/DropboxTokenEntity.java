package com.sjkb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.admin.DropboxReturnToken;

import org.springframework.http.ResponseEntity;

@Entity(name = "dropboxToken")
@Table(name="dropbox")
public class DropboxTokenEntity {

    @Id
    public String user;

    @Column(name="user_id")
    public String userId;

    public String token;

    public DropboxTokenEntity() {};

    public DropboxTokenEntity(ResponseEntity<DropboxReturnToken> response) {
        this.userId = response.getBody().getAccount_id();
        this.token = response.getBody().getAccess_token();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    
    

}