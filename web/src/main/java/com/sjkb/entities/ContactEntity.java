package com.sjkb.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.UserViewModel;

@Entity(name="contact")
@Table(name="contact")
public  class ContactEntity {
    
    @Id
    private String uid;
    private String firstname;
    private String lastname;
    private String company;
    private String email;
    private String phone;
    private String street;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String username;
    private String account;
    private String role;
    private String context;
    private int branch;
    private Timestamp lastLogin;
    private Timestamp created;

    public ContactEntity() {}

    public ContactEntity(UserViewModel userModel) {
        this.uid = UUID.randomUUID().toString();
        this.username = userModel.getUsername();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.email = userModel.getEmail();
        this.phone = userModel.getPhone();
        this.role = userModel.getRole();
        this.city = userModel.getCity();
        this.state = userModel.getState();
        this.created = new Timestamp(System.currentTimeMillis());
        this.street = userModel.getStreet();
        this.street2 = userModel.getStreet2();
        this.zip = userModel.getZip();
        this.company = userModel.getCompany();

	}

	public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCompany() {
        if (company == null || company.length() == 0) {
            return this.lastname;
        }
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

   
    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

	public void copyFromContact(UserViewModel userModel) {

        this.username = userModel.getUsername();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.email = userModel.getEmail();
        this.phone = userModel.getPhone();
        this.role = userModel.getRole();
        this.city = userModel.getCity();
        this.state = userModel.getState();
        this.street = userModel.getStreet();
        this.street2 = userModel.getStreet2();
        this.zip = userModel.getZip();
        this.company = userModel.getCompany();
	}

    
    
}