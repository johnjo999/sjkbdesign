package com.sjkb.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl extends User implements UserDetails {

    private static final long serialVersionUID = 4520944443430469957L;

    public UserDetailsImpl(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role-> new SimpleGrantedAuthority(super.getRole()))
                .collect(Collectors.toList());
    }

    private Collection<? extends GrantedAuthority> getRoles() {
        Collection<GrantedAuthority> roles = new ArrayList<>();
        GrantedAuthority authority = new GrantedAuthority(){
        
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority() {
                return getRole();
            }
        };
        roles.add(authority);
        return roles;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
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
        return true;
    }
}