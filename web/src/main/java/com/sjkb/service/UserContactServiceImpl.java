package com.sjkb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.category.ContractorSelectRow;
import com.sjkb.models.users.UserDelModel;
import com.sjkb.models.users.UserRoleModel;
import com.sjkb.models.users.UserViewModel;
import com.sjkb.repositores.CompanyRepsInterface;
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

    private String context;

    // cached ID handler used as alternate reference to username;
    private Map<String, UserViewModel> userTokenMap = new HashMap<>();

    @Override
    public List<UserViewModel> getAllUsers(String role) {
        List<UserViewModel> result = new ArrayList<>();
        List<ContactEntity> contacts = contactRepository.findByContextAndRoleOrderByLastname(context, role);
        if (contacts != null) {
            userTokenMap.clear();
            for (ContactEntity contact : contacts) {
                UserViewModel userModel = new UserViewModel(contact);
                userTokenMap.put(userModel.getToken(), userModel);
                result.add(userModel);
            }
        }
        return result;
    }

    /**
     * Stores a new user to the spring security user table. Creates a default
     * password if none is present
     * 
     * @param userNewModel
     * @param createdBy
     * @return true is user the user account was updated rather than created
     * @throws UsernameTakenException
     */
    private boolean createUserAccount(UserViewModel userNewModel, String createdBy) throws UsernameTakenException {
        boolean result = true;
        if (userNewModel.getUsername().length() > 2) {
            Optional<UserEntity> user = userRepository.findByUsername(userNewModel.getUsername());
            UserEntity userEntity = null;
            if (user.isPresent() == true) {
                if (userNewModel.getToken() == null) {
                    throw new UsernameTakenException(String.format("user %s not found", userNewModel.getUsername()));
                } else {
                    userRepository.delete(user.get());
                    result = false;
                }
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
        return result;
    }

    /**
     * Creates new User in Contact DB
     */

    @Override
    public String addNewUser(UserViewModel userNewModel, String createdBy) throws UsernameTakenException {
        ContactEntity contactEntity = null;
        if (userNewModel.getUsername().length() == 0 && userNewModel.getEmail().length() > 3) {
            userNewModel.setUsername(userNewModel.getEmail());
        }
        if (createUserAccount(userNewModel, createdBy) == false) {
            contactEntity = contactRepository.findByUsername(userNewModel.getUsername());
        }
        if (contactEntity == null)
            contactEntity = new ContactEntity(userNewModel);
        else {
            contactEntity.copyFromContact(userNewModel);
        }
        contactEntity.setBranch(1);
        contactEntity.setContext(context);
        contactRepository.save(contactEntity);
        return contactEntity.getUid();
    }

    public String createDbxFolder(UserViewModel userNewModel, String createdBy) {
        long empid = 0L;
        int locid = 0;
        int year = 19;
        String foldername = "";
        Optional<UserEntity> optemp = userRepository.findByUsername(createdBy);
        if (optemp.isPresent()) {
            UserEntity emp = optemp.get();
            empid = emp.getId();
            ContactEntity sponsor = contactRepository.findByUsername(createdBy);
            locid = sponsor.getBranch();
        }
        if (userNewModel.getRole().equals(UserRoleModel.ROLES.customer.toString())) {
            foldername = String.format("%s_%02d%02d-%d", userNewModel.getLastname(), year, locid, empid).toLowerCase();
            if (userNewModel.isNewUser()) {
                try {
                    dropboxService.createFolder(createdBy, foldername);
                    Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userNewModel.getUsername());
                    if (optionalUserEntity.isPresent()) {
                        UserEntity userEntity = optionalUserEntity.get();
                        userEntity.setDbxFolder(foldername);
                        userRepository.save(userEntity);
                    }

                } catch (DbxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return foldername;
    }

    /*
     * @Override public List<ContactEntity> getAllUser() { return
     * contactRepository.findAll(); }
     */
    @Override
    public void remove(String empId, UserDelModel userDelModel) throws DeleteErrorException, DbxException {
        ContactEntity contact = contactRepository.findByUid(userDelModel.getUid());
        if (contact == null)
            return;
        String uname = contact.getUsername();

        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(uname);

        if ("s".equals(userDelModel.getType()) && userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            userEntity.setLocked(true);
            userRepository.save(userEntity);
        }
        if ("d".equals(userDelModel.getType())) {
            contactRepository.delete(contact);
            if (userEntityOpt.isPresent()) {
                UserEntity userEntity = userEntityOpt.get();
                userRepository.delete(userEntity);
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

    @Override
    public UserViewModel getByToken(String token) {
        return userTokenMap.get(token);
    }

    @Override
    public ContactEntity getContactByUid(String pocId) {
        return contactRepository.findByUid(pocId);
    }

    @Override
    public List<ContractorSelectRow> getContratorSelectRows() {
        List<ContractorSelectRow> result = new ArrayList<>();
        List<ContactEntity> contractors = contactRepository.findByContextAndRole("contractor", context);
        if (contractors != null && contractors.size() > 0) {
            for (ContactEntity contractor : contractors) {
                ContractorSelectRow row = new ContractorSelectRow(contractor);
                result.add(row);
            }

        }
        ContractorSelectRow newcontractor = new ContractorSelectRow();
        newcontractor.setId("createnew");
        newcontractor.setName("New Contractor");
        result.add(newcontractor);
        return result;
    }

    @Override
    public List<ContractorSelectRow> getInstallerSelectRows() {
        List<ContractorSelectRow> result = new ArrayList<>();
        List<ContactEntity> contractors = contactRepository.findByContextAndRole("installer", context);
        if (contractors != null && contractors.size() > 0) {
            for (ContactEntity contractor : contractors) {
                ContractorSelectRow row = new ContractorSelectRow(contractor);
                result.add(row);
            }

        }
        ContractorSelectRow newcontractor = new ContractorSelectRow();
        newcontractor.setId("createnew");
        newcontractor.setName("New Installer");
        result.add(newcontractor);
        return result;
    }

    @Override
    public void setContext(String context) {
        this.context = context;

    }

    /***
     * Creates new context Input, Admin of the new context, Sponsor user creating
     * the context Output, the UID of the newly created context. Company name should
     * be set by second dialog in the workflow
     * 
     * @throws UsernameTakenException
     */
    @Override
    public String createContextForNewUser(UserViewModel userNewModel, String uname) throws UsernameTakenException {
        ContactEntity contactEntity = null;
        String contextUid = UUID.randomUUID().toString();
        if (userNewModel.getUsername().length() == 0 && userNewModel.getEmail().length() > 3) {
            userNewModel.setUsername(userNewModel.getEmail());
        }
        if (createUserAccount(userNewModel, uname) == false) {
            contactEntity = contactRepository.findByUsername(userNewModel.getUsername());
        }
        if (contactEntity == null)
            contactEntity = new ContactEntity(userNewModel);
        else {
            contactEntity.copyFromContact(userNewModel);
        }
        contactEntity.setContext(contextUid);
        contactEntity.setBranch(1);
        contactRepository.save(contactEntity);
        return contextUid;
    }

    @Override
    public String getUidForUsername(String username) {
        return contactRepository.findByUsername(username).getUid();
    }

    @Override
    public List<CompanyRepsInterface> getCompaniesWithReps(String context) {
        return contactRepository.findCompanyByContext(context);
    }

}