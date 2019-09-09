package com.sjkb.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.dropbox.core.DbxException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.models.GetFileModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.UserRepository;
import com.sjkb.service.DropboxService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/portal")
public class PortalController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserContactService userService;

    @Autowired
    DropboxService dropboxService;

    @Autowired
    UserRepository userRepository;

    final String portalToken = UUID.randomUUID().toString().replaceAll("-", "3");
    String fileOnDeck = null;

    @RequestMapping(value = "dashboard")
    public String dashboard(ModelMap map, @RequestHeader Map<String, String> header, Authentication authentication) {

        boolean emp = false;
        boolean sys = false;

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> grants = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = grants.iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            if (auth.getAuthority().equals("ROLE_USER") || auth.getAuthority().equals("ROLE_ADMIN"))
                emp = true;
            else if (auth.getAuthority().equals("ROLE_SYS"))
                sys = true;
        }
        if (emp)
            return "redirect:/backstage/dashboard";
        if (sys)
            return "redirect:/sysadm/dashboard";
        ContactEntity contact = contactRepository.findByUsername(username);
        map.addAttribute("token", portalToken);
        ContactEntity sponsor = userService.getSponserFor(username);
        if (sponsor != null) {
            map.addAttribute("designer", sponsor.getFirstname());
        } else {
            map.addAttribute("designer", "someone");
        }

        if (contact == null) {
            map.addAttribute("user", "guest");
        } else
            map.addAttribute("user", contact.getFirstname());
        return "portal";
    }

    @RequestMapping(value = "shared/{token}")
    public String getSharedFiles(ModelMap map, @PathVariable("token") final String token) throws DbxException {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (token.equals(portalToken)) {
            Optional<UserEntity> userEntityOption = userRepository.findByUsername(uname);
            if (userEntityOption.isPresent()) {
                UserEntity userEntity = userEntityOption.get();
                List<FileHandleModel> files = dropboxService.getFilesSharedFor(userEntity.getSponsor(),
                        userEntity.getDbxFolder());
                map.addAttribute("files", files);
            }
        }
        return "fragments/portal_templates::file";
    }

    @RequestMapping(value = "getfile", method = RequestMethod.POST)
    public String getFile(ModelMap map, HttpServletResponse response, @RequestBody final GetFileModel getFileModel) {
        if (getFileModel.getToken().equals(portalToken) == false || getFileModel.getFilename() == null) {
            return "redirect:/home";
        }
        // dbx supported previews
        String[] pdfFiles = { ".pdf", ".ai", ".doc", ".docm", ".docx", ".eps", ".gdoc", ".gslides", ".odp", ".odt",
                ".pps", ".ppsm", ".ppsx", ".ppt", ".pptm", ".pptx", ".rtf", ".jpg"};
        String[] htmlPreview = { "csv", ".ods", ".xls", ".xlsm", ".gsheet", ".xlsx" };
        Set<String> byteResponse = new HashSet<>(Arrays.asList(pdfFiles));
        Set<String> multilineResponse = new HashSet<>(Arrays.asList(htmlPreview));
        String filename = getFileModel.getFilename();
        int fn = filename.indexOf('.');
        String result = "";
        if (fn > 0) {
            String[] sharePath = getSharePath(getFileModel.getToken());
            if (sharePath[0] == null || sharePath[1] == null) {
                return "";
            }
            try {
                if (byteResponse.contains(filename.substring(fn))) {
                    map.addAttribute("link", "getBinFile");
                    fileOnDeck = filename;
                    result = "fragments/portal_templates::redirect";
                } else if (multilineResponse.contains(filename.substring(fn))) {
                    String preview = dropboxService.getPreviewOf(sharePath[0], sharePath[1], filename);
                    map.addAttribute("preview", preview);
                    result = "/fragments/portal_templates::linepreview";
                } else {
                    String filelink = dropboxService.getFileLink(sharePath[0], sharePath[1], filename);
                    if (filelink != null) {
                        map.addAttribute("link", filelink);
                        result = "fragments/portal_templates::redirect";
                    }
                }
            } catch (DbxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;

    }

    private String[] getSharePath(String token) {
        String[] result = new String[2];
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (token.equals(portalToken)) {
            Optional<UserEntity> userEntityOption = userRepository.findByUsername(uname);
            if (userEntityOption.isPresent()) {
                UserEntity userEntity = userEntityOption.get();
                result[0] = userEntity.getSponsor();
                result[1] = userEntity.getDbxFolder();
            }
        }
        return result;
    }

    
    @RequestMapping(value = "gettoken", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getBinFile(ModelMap map, @RequestParam("token") final String reqToken) {
        if (reqToken.equals(portalToken) == false || fileOnDeck == null) {
            return null;
        }

        String[] sharePath = getSharePath(reqToken);
            if (sharePath[0] == null || sharePath[1] == null) {
                return null;
            }
        InputStreamResource inStreamResource = null;
        final HttpHeaders headers = new HttpHeaders();
        try {
        if (fileOnDeck.endsWith(".docx")) {
            inStreamResource = dropboxService.getPreviewOf(sharePath[0], sharePath[1], fileOnDeck, headers);
        } else {
            inStreamResource = dropboxService.getFile(sharePath[0], sharePath[1], fileOnDeck, headers);
        }
        } catch (DbxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     //   fileOnDeck = null;
        // return new ResponseEntity<InputStreamResource>(inStreamResource, headers, HttpStatus.OK);
        return ResponseEntity.ok().headers(headers).body(inStreamResource);
    }

}