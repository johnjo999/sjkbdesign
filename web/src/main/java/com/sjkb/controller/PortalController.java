package com.sjkb.controller;

import java.util.Collection;
import java.util.Iterator;

import com.sjkb.entities.ContactEntity;
import com.sjkb.repositores.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/portal")
public class PortalController {

    @Autowired
    ContactRepository contactRepository;

    @RequestMapping(value = "dashboard")
    public String dashboard(ModelMap map, Authentication authentication) {
        boolean emp = false;
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> grants = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = grants.iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            if (auth.getAuthority().equals("ROLE_USER"))
                emp = true;
        }
        if (emp)
            return "redirect:/backstage/dashboard";
        ContactEntity contact = contactRepository.findByUsername(username);
        map.addAttribute("user", contact.getFirstname());
        return "portal";
    }

}