package com.sjkb.service;

import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.category.ContractorSelectRow;
import com.sjkb.models.users.UserDelModel;
import com.sjkb.models.users.UserViewModel;

public interface UserContactService {

    public List<UserViewModel> getAllUsers();

	/**
	 * 
	 * @param userNewModel
	 * @param createdBy
	 * @return JobId, also the dropbox folder
	 * @throws UsernameTakenException
	 */
	public String addNewUser(UserViewModel userNewModel, String createdBy) throws UsernameTakenException;

//	public List<ContactEntity> getAllUser();

	public void remove(String empId, UserDelModel userDelModel) throws DeleteErrorException, DbxException;

	public ContactEntity getContactByUserid(String username);

	public ContactEntity getSponserFor(String username);

	public UserViewModel getByToken(String token);

	public ContactEntity getContactByUid(String pocId);

	public List<ContractorSelectRow> getContratorSelectRows();

	public List<ContractorSelectRow> getInstallerSelectRows();

	public void setContext(String context);

	public String createDbxFolder(UserViewModel userModel, String uname);

	public String createContextForNewUser(UserViewModel userModel, String uname) throws UsernameTakenException;

	public String getUidForUsername(String username);


}