package com.sjkb.controller;

import java.util.UUID;

import com.sjkb.entities.ContextEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.users.UserNewModel;
import com.sjkb.models.users.UserViewModel;
import com.sjkb.service.UserContactService;
import com.sjkb.service.sysadmin.SysAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/sysadm")
public class SysAdminController {

    @Autowired SysAdminService sysAdminService;

    @Autowired UserContactService userContactService;

    private String key;

    private String getUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        return uname;
    }

    @RequestMapping(value = "dashboard")
    public String getSysAdminDashboard(ModelMap map) {
        SecurityContext holder = SecurityContextHolder.getContext();
        java.util.Iterator<? extends GrantedAuthority> roles = holder.getAuthentication().getAuthorities().iterator();
        String role = null;
        if (roles.hasNext())
            role = roles.next().getAuthority();
        if ("ROLE_SYS".equals(role)) {
            map.addAttribute("contexts", sysAdminService.getAllAccounts());
        }
        return "sysadmin/syshome";
    }

    @RequestMapping(value = "context/add", method = RequestMethod.GET)
    public String getContextAdd(ModelMap map) {
        UserNewModel user = new UserNewModel();
        key = UUID.randomUUID().toString();
        user.setKey(key);
        map.addAttribute("newuser", user);
        map.addAttribute("user", getUser());
        map.addAttribute("action", "/sysadm/context/add");
        map.addAttribute("roles", "administrator");
        return "users_new";
    }

    @RequestMapping(value = "context/add", method = RequestMethod.POST)
    public String postContextAdd(ModelMap map, @ModelAttribute("user") final UserViewModel userModel) {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (key.equals(userModel.getKey()))
            try {
                String contextId = userContactService.createContextForNewUser(userModel, uname);
                ContextEntity context = new ContextEntity();
                context.setUid(contextId);
                context.setAdmin(userContactService.getUidForUsername(userModel.getUsername()));
                context.setName(userModel.getCompany());
                if (context.getName() != null && context.getName().length() > 3)
                sysAdminService.createCompany(context);
                else {
                    map.addAttribute("problem", "Must have company name");
                    map.addAttribute("backpage", "/sysadm/context/add");
                return "problem";
                }
            return "redirect:/sysadm/dashboard";      
            } catch (UsernameTakenException e) {
                return "users_error_taken";
            }             
        return "redirect:/home"; 
    }
}