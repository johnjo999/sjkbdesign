package com.sjkb.service.sysadmin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.ContextEntity;
import com.sjkb.models.AccountModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.ContextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysAdminServiceImpl implements SysAdminService {

    @Autowired
    ContextRepository contextRepository;

    @Autowired
    ContactRepository contactRepository;

    @Override
    public List<AccountModel> getAllAccounts() {
        List<AccountModel> result = new ArrayList<>();
        List<ContextEntity> accounts = contextRepository.findAll();
        for (ContextEntity account : accounts) {
            AccountModel accountModel = new AccountModel();
            accountModel.setCompany(account.getName());
            Optional<ContactEntity> optionalContact = contactRepository.findById(account.getAdmin());
            if (optionalContact.isPresent()) {
                ContactEntity contact = optionalContact.get();
                accountModel.setPocUid(account.getAdmin());
                accountModel.setPoc(String.format("%s, %s", contact.getLastname(), contact.getFirstname()));

                result.add(accountModel);
            }
        }
        return result;
    }

    @Override
    public void createCompany(ContextEntity contextEntity) {
        if (contextEntity.getCreated() == null) {
            contextEntity.setCreated(new Timestamp(System.currentTimeMillis()));
        }
        contextRepository.save(contextEntity);

    }

}