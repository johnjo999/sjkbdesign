package com.sjkb.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sjkb.entities.UserEntity;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserComponent implements UserDetails {

    private static final long serialVersionUID = -847703295129427081L;
    private UserEntity user;

    public UserComponent(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority granted = new GrantedAuthority(){
        
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority() {
                return user.getRole();
            }
        };
        List<GrantedAuthority> grants = new ArrayList<>();
        grants.add(granted);

        return grants;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isLocked();
    }

	public User getUserDetails() {
		return null;
    }

    public UserEntity getUser() {
        return user;
    }
    
}