package com.sjkb.service;

import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.sjkb.entities.DropboxTokenEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.models.admin.DropboxTokenModel;
import com.sjkb.repositores.DropboxTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DropboxServiceImpl implements DropboxService {

    @Autowired
    DropboxTokenRepository dropboxRepository;

    @Override
    public List<DropboxTokenModel> getAllUserTokens() {
        List<DropboxTokenEntity> tokens = dropboxRepository.findAll();
        List<DropboxTokenModel> result = new ArrayList<>();
        for (DropboxTokenEntity token : tokens) {
            result.add(new DropboxTokenModel(token));
        }
        return result;
    }

    @Override
    public void saveToken(DropboxTokenEntity token) {
        dropboxRepository.save(token);
    }

    @Override
    public void createFolder(String username, String foldername) throws CreateFolderErrorException, DbxException {
        List<DropboxTokenEntity> tokens = dropboxRepository.findAll();
        for (DropboxTokenEntity token : tokens) {
            if (token.getUser().equals(username)) {
                DbxRequestConfig config = DbxRequestConfig.newBuilder("sjkb/folder1.0").build();
                DbxClientV2 client = new DbxClientV2(config, token.getToken());
                client.files().createFolderV2("/" + foldername, true);
                client.files().createFolderV2("/" + foldername + "/shared", true);
            }
        }
    }

    @Override
    public void removeFolderFor(String empId, String folder) throws DbxException {
        if (folder == null || folder.length()<4)
        return;
        DbxClientV2 client = getClientFor(empId);
        if (client != null) {
                client.files().deleteV2("/" + folder);
        }
    }

    @Override
    public List<FileHandleModel> getFilesSharedFor(String sponsor, String dbxFolder) throws DbxException {
        List<FileHandleModel> files = new ArrayList<>();
        ListFolderResult folderResult = null;
        DbxClientV2 client = getClientFor(sponsor);
        if (client != null) {
            folderResult = client.files().listFolder("/" + dbxFolder + "/shared");
            for (Metadata meta : folderResult.getEntries()) {
                FileHandleModel file = new FileHandleModel();
                file.setFilename(meta.getName());
                files.add(file);
            }
        }
        return files;
    }

    private DbxClientV2 getClientFor(String user) {
        DbxClientV2 client = null;
        List<DropboxTokenEntity> tokens = dropboxRepository.findByUser(user);
        if (tokens.size() == 1) {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("sjkb/folder1.0").build();
            client = new DbxClientV2(config, tokens.get(0).getToken());
        }
        return client;
    }

}