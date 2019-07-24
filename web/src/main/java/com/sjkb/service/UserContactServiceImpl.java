package com.sjkb.service;

import java.util.ArrayList;
import java.util.List;

import com.sjkb.entities.ContactEntity;
import com.sjkb.models.UserViewModel;
import com.sjkb.repositores.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class UserContactServiceImpl implements UserContactService {

    @Autowired
    ContactRepository contactRepository;

	@Override
	public List<UserViewModel> getAllUsers() {
        List<UserViewModel> result = new ArrayList<>();
        List<ContactEntity> contacts = contactRepository.findAll();
        if (contacts != null) {
            for (ContactEntity contact : contacts) {
                result.add(new UserViewModel(contact));
            }
        }
        return result;
	}
    
}