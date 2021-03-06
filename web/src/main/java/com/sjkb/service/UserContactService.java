package com.sjkb.service;

import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.category.ContractorSelectRow;
import com.sjkb.models.users.UserDelModel;
import com.sjkb.models.users.UserViewModel;
import com.sjkb.repositores.CompanyRepsInterface;

public interface UserContactService {

    public List<UserViewModel> getAllUsers(String type);

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

	public UserViewModel getByUid(String token);

	public ContactEntity getContactByUid(String pocId);

	public List<ContractorSelectRow> getContratorSelectRows();

	public List<ContractorSelectRow> getInstallerSelectRows();

	public String getContext();

	public String createDbxFolder(UserViewModel userModel, String uname);

	public String createContextForNewUser(UserViewModel userModel, String uname) throws UsernameTakenException;

	public String getUidForUsername(String username);

	public List<CompanyRepsInterface> getCompaniesWithReps(String context);

	public void clearContext();

	public String getUserIs();

	public String getUserId();

	public void setContext();

	public String getUsernameFor(String user);

	public List<ContractorSelectRow> getCabinetSelectRows();


}