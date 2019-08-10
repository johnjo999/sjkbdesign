package com.sjkb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.UserDelModel;
import com.sjkb.models.UserNewModel;
import com.sjkb.models.UserRoleModel;
import com.sjkb.models.UserViewModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserContactServiceImpl implements UserContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DropboxService dropboxService;

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

    @Override
    public String addNewUser(UserNewModel userNewModel, String createdBy) throws UsernameTakenException {
        Optional<UserEntity> optemp = userRepository.findByUsername(createdBy);
        long empid = 0L;
        int locid = 0;
        int year = 19;
        UserEntity userEntity = null;
        String jobid = "";
        if (optemp.isPresent()) {
            UserEntity emp = optemp.get();
            empid = emp.getId();
            ContactEntity sponsor = contactRepository.findByUsername(createdBy);
            locid = sponsor.getBranch();
        }
        if (userNewModel.getUsername().length() == 0 && userNewModel.getEmail().length() > 3) {
            userNewModel.setUsername(userNewModel.getEmail());
        }
        if (userNewModel.getUsername().length() > 2) {
            Optional<UserEntity> user = userRepository.findByUsername(userNewModel.getUsername());
            if (user.isPresent() == true) {
                throw new UsernameTakenException(String.format("user %s not found", userNewModel.getUsername()));
            }
            if (userNewModel.getPassword().length() == 0) {
                int i = userNewModel.getFirstname().length();
                int j = userNewModel.getPhone().length();
                if (i > 4)
                    i = 4;
                if (j < 4)
                    j = 4;
                String fn = userNewModel.getFirstname().substring(0, i).toLowerCase();
                String ph = userNewModel.getPhone().substring(j - 4);
                userNewModel.setPassword(fn + ph);
            }
            if (userNewModel.getPassword().length() > 2) {
                userEntity = new UserEntity();
                userEntity.encodePwd(userNewModel.getPassword());
                userEntity.setUsername(userNewModel.getUsername());
                userEntity.setLocked(false);
                userEntity.setRole(UserRoleModel.getUserRole(userNewModel.getRole()));
                userEntity.setSponsor(createdBy);
                userRepository.save(userEntity);
            }
        }
        ContactEntity contactEntity = new ContactEntity(userNewModel);
        contactEntity.setBranch(1);
        if (userNewModel.getRole().equals(UserRoleModel.ROLES.customer.toString())) {
            String foldername = String.format("%s_%02d%02d-%d", userNewModel.getLastname(), year, locid, empid).toLowerCase();
            jobid = foldername;
            try {
                dropboxService.createFolder(createdBy, foldername);
                if (userEntity != null) {
                    userEntity.setDbxFolder(foldername);
                }

            } catch (DbxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        contactRepository.save(contactEntity);
        return jobid;
    }

    @Override
    public List<ContactEntity> getAllUser() {
        return contactRepository.findAll();
    }

    @Override
    public void remove(String empId, UserDelModel userDelModel) throws DeleteErrorException, DbxException {
        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(userDelModel.getUsername());
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            ContactEntity contact = contactRepository.findByUsername(userDelModel.getUsername());
            if ("s".equals(userDelModel.getType())) {
                userEntity.setLocked(true);
                userRepository.save(userEntity);
            }
            if ("d".equals(userDelModel.getType())) {
                userRepository.delete(userEntity);
                contactRepository.delete(contact);
                if (userDelModel.isDelDropbox() && userEntity.getDbxFolder() != null) {
                    dropboxService.removeFolderFor(empId, userEntity.getDbxFolder());
                }
            }
        }
    }

    @Override
    public ContactEntity getContactByUserid(String username) {
        return contactRepository.findByUsername(username);
    }

    @Override
    public ContactEntity getSponserFor(String username) {
        ContactEntity result = null;
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            result = contactRepository.findByUsername(userOpt.get().getSponsor());
        }
        return result;
    }

}