package com.sjkb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="context")
public class ContextEntity {

    @Id
    private String uid;

    private String name;

    private String admin;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

    
}