package com.sjkb.service;

import java.util.Optional;

import com.sjkb.repositores.UserRepository;
import com.sjkb.components.UserComponent;
import com.sjkb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalEntity = userRepository.findByUsername(username);
        UserComponent user = null;
        if (optionalEntity.isPresent())
            user = new UserComponent(optionalEntity.get());
        else
            throw new UsernameNotFoundException("User not found: "+username);
        return user;
    }
    
}