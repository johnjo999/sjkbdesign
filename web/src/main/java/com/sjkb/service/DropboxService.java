package com.sjkb.service;

import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.sjkb.entities.DropboxTokenEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.models.admin.DropboxTokenModel;

import org.springframework.stereotype.Service;

@Service
public interface DropboxService {
    public List<DropboxTokenModel> getAllUserTokens();

	public void saveToken(DropboxTokenEntity token);

	public void createFolder(String empName, String lastname) throws CreateFolderErrorException, DbxException;

	public void removeFolderFor(String empId, String username) throws DeleteErrorException, DbxException;

	public List<FileHandleModel> getFilesSharedFor(String sponsor, String dbxFolder) throws DbxException;

}