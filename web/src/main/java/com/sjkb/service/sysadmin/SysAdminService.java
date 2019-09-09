package com.sjkb.service.sysadmin;

import java.util.List;

import com.sjkb.entities.ContextEntity;
import com.sjkb.models.AccountModel;

public interface SysAdminService {
    public List<AccountModel> getAllAccounts();

	public void createCompany(ContextEntity contextEntity);
}