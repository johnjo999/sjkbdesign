package com.sjkb.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ReadException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadUploader;
import com.sjkb.entities.DropboxTokenEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.models.admin.DropboxTokenModel;
import com.sjkb.repositores.DropboxTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class DropboxServiceImpl implements DropboxService {

    @Autowired
    DropboxTokenRepository dropboxRepository;

    @Override
    public List<DropboxTokenModel> getAllUserTokens(String context) {
        List<DropboxTokenEntity> tokens = dropboxRepository.findByContext(context);
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
        DropboxTokenEntity token = dropboxRepository.findByUser(username);
        if (token != null) {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("sjkb/folder1.0").build();
            DbxClientV2 client = new DbxClientV2(config, token.getToken());
            client.files().createFolderV2("/" + foldername, true);
            client.files().createFolderV2("/" + foldername + "/shared", true);
        }
    }

    @Override
    public void removeFolderFor(String empId, String folder) throws DbxException {
        if (folder == null || folder.length() < 4)
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
        if (client != null && dbxFolder != null) {
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
        DropboxTokenEntity token = dropboxRepository.findByUser(user);
        if (token != null) {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("sjkb/folder1.0").build();
            client = new DbxClientV2(config, token.getToken());
        }
        return client;
    }

    @Override
    public FileMetadata getPreviewOf(String user, String folder, String filename, OutputStream out)
            throws DbxException {
        String path = "/" + folder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        DbxDownloader<FileMetadata> holder = client.files().getPreview(path);
        FileMetadata result = holder.getResult();
        try {
            holder.download(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public InputStreamResource getFile(String user, String folder, String filename, HttpHeaders headers)
            throws DbxException {
        String path = "/" + folder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        DbxDownloader<FileMetadata> handle = client.files().download(path);
        // String[] mt = handle.getContentType().split("/");
        // headers.setContentType(new MediaType(mt[0], mt[1]));
        MediaType type = MediaType.APPLICATION_OCTET_STREAM;
        headers.setContentType(type);
        InputStreamResource inputStreamResource = null;

        InputStream instream = handle.getInputStream();
        inputStreamResource = new InputStreamResource(instream);
        headers.setContentLength(handle.getResult().getSize());

        return inputStreamResource;
    }

    @Override
    public GetTemporaryLinkResult getFileLink(String user, String folder, String filename) throws DbxException {
        String path = "/" + folder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        GetTemporaryLinkResult link = client.files().getTemporaryLink(path);
        return link;
    }

    @Override
    public String getPreviewOf(String user, String folder, String filename) throws DbxException {
        String result = "";
        String path = "/" + folder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        DbxDownloader<FileMetadata> holder = client.files().getPreview(path);
        InputStream inStream = holder.getInputStream();
        try {

            result = IOUtil.toUtf8String(inStream);
        } catch (ReadException | CharacterCodingException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public InputStreamResource getPreviewOf(String user, String folder, String filename, HttpHeaders headers)
            throws DbxException {
        String path = "/" + folder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        DbxDownloader<FileMetadata> handle = client.files().getPreview(path);
        String[] mt = handle.getContentType().split("/");
        headers.setContentType(new MediaType(mt[0], mt[1]));
        InputStreamResource inputStreamResource = null;

        InputStream instream = handle.getInputStream();
        inputStreamResource = new InputStreamResource(instream);

        return inputStreamResource;
    }

    /**
     * Gets an output stream from dropbox that can be used to write files
     * 
     * @param: dbxFoler, The directory path
     * @param: filename, The name of the file user: The dropbox user account holding
     *                   the auth token
     */
    @Override
    public UploadUploader getOutputFileStream(String dbxFolder, String filename, String user) throws DbxException {
        String path = "/" + dbxFolder + "/shared/" + filename;
        DbxClientV2 client = getClientFor(user);
        UploadUploader handle = client.files().upload(path);
        return handle;
    }

}