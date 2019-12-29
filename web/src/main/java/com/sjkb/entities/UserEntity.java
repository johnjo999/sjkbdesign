package com.sjkb.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.DigestUtils;

@Entity(name = "user")
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String username;
    private String usernameCry;
    @NotEmpty
    private String password;
    private Date dateCreated;
    private String role;
    private boolean locked;
    private String sponsor;
    private String jobid;

    @Transient
    private TextEncryptor crypter = Encryptors.text("thesjkbkey", "AE7387");

    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setUseridClear(String pwdClear) {
        this.username = DigestUtils.md5DigestAsHex(pwdClear.getBytes());
        this.usernameCry = crypter.encrypt(pwdClear);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

	public void encodePwd(String pwd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(pwd);
        this.locked = false;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.setRole("ROLE_USER");
        
	}
    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getUsernameCry() {
        return usernameCry;
    }

    public void setUsernameCry(String usernameCry) {
        this.usernameCry = usernameCry;
    }

    public String getUsernameClear() {
        return crypter.decrypt(usernameCry);
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    
}
