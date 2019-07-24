package com.sjkb.models;

import com.sjkb.entities.ContactEntity;

public class UserViewModel {
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String lastLogin;
    private String street;
    private String city;
    private String state;
    private String zip;
    private boolean disabled;

    public UserViewModel() {};

    public UserViewModel(ContactEntity contact) {
        this.username = contact.getUsername();
        this.firstname = contact.getFirstname();
        this.lastname = contact.getLastname();
        this.phone = contact.getPhone();
        this.email = contact.getEmail();
        this.street = contact.getStreet();
        this.city = contact.getCity();
        this.state = contact.getState();
        this.zip = contact.getZip();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
 
    
    
}