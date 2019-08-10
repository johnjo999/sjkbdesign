package com.sjkb.service;

import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.UserDelModel;
import com.sjkb.models.UserNewModel;
import com.sjkb.models.UserViewModel;

public interface UserContactService {

    public List<UserViewModel> getAllUsers();

	/**
	 * 
	 * @param userNewModel
	 * @param createdBy
	 * @return JobId, also the dropbox folder
	 * @throws UsernameTakenException
	 */
	public String addNewUser(UserNewModel userNewModel, String createdBy) throws UsernameTakenException;

	public List<ContactEntity> getAllUser();

	public void remove(String empId, UserDelModel userDelModel) throws DeleteErrorException, DbxException;

	public ContactEntity getContactByUserid(String username);

	public ContactEntity getSponserFor(String username);

}