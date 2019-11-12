package com.sjkb.service;

import java.io.OutputStream;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.UploadUploader;
import com.sjkb.entities.DropboxTokenEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.models.admin.DropboxTokenModel;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public interface DropboxService {
    public List<DropboxTokenModel> getAllUserTokens(String context);

	public void saveToken(DropboxTokenEntity token);

	public void createFolder(String empName, String lastname) throws CreateFolderErrorException, DbxException;

	public void removeFolderFor(String empId, String username) throws DeleteErrorException, DbxException;

	public List<FileHandleModel> getFilesSharedFor(String sponsor, String dbxFolder) throws DbxException;

	public FileMetadata getPreviewOf(String user, String folder, String filename, OutputStream out) throws DbxException;

	public String getPreviewOf(String user, String folder, String filename) throws DbxException;

	public InputStreamResource getPreviewOf(String user, String folder, String filename, HttpHeaders headers) throws DbxException;

	public InputStreamResource getFile(String user, String folder, String filename, HttpHeaders headers) throws DbxException ;

	public GetTemporaryLinkResult getFileLink(String user, String folder, String filename) throws DbxException ;

	public UploadUploader getOutputFileStream(String dbxFolder, String jobid, String user) throws DbxException;

}