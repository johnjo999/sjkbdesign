package com.sjkb.models.users;


import com.sjkb.entities.ContactEntity;

public class UserViewModel {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String lastLogin;
    private String street;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private boolean disabled;
    private String key;
    private String company;
    private String token;
    private String role;
    private boolean newUser = true;;

    public UserViewModel() {};

    public UserViewModel(ContactEntity contact) {
        this.username = contact.getUsername();
        this.firstname = contact.getFirstname();
        this.lastname = contact.getLastname();
        this.phone = contact.getPhone();
        this.email = contact.getEmail();
        this.street = contact.getStreet();
        this.street2 = contact.getStreet2();
        this.city = contact.getCity();
        this.state = contact.getState();
        this.zip = contact.getZip();
        this.role = contact.getRole();
        this.company = contact.getCompany();
        this.token = contact.getUid();
        this.newUser = false;
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

	public String getToken() {
		return token;
	}

    public UserViewModel setToken(String token) {
        this.token = token;
        return this;
    }

    public String getPassword() {
       return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getKey() {
        return key;
    }

    public UserViewModel setKey(String key) {
        this.key = key;
        return this;
    }

    public String getCompany() {
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

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    
    
    
}