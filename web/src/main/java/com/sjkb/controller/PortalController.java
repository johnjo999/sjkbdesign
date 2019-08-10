package com.sjkb.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.dropbox.core.DbxException;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.models.FileHandleModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.UserRepository;
import com.sjkb.service.DropboxService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "dashboard")
    public String dashboard(ModelMap map, @RequestHeader Map<String, String> header, Authentication authentication) {

        boolean emp = false;

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> grants = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = grants.iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            if (auth.getAuthority().equals("ROLE_USER") || auth.getAuthority().equals("ROLE_ADMIN"))
                emp = true;
        }
        if (emp)
            return "redirect:/backstage/dashboard";
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
                List<FileHandleModel> files = dropboxService.getFilesSharedFor(userEntity.getSponsor(), userEntity.getDbxFolder());
                map.addAttribute("files", files);
            }
        }
        return "fragments/portal_templates::file";
    }

}