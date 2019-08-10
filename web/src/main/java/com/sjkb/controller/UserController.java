package com.sjkb.controller;

import java.util.Iterator;
import java.util.UUID;

import com.dropbox.core.DbxException;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.UserDelModel;
import com.sjkb.models.UserNewModel;
import com.sjkb.models.UserRoleModel;
import com.sjkb.repositores.UserRepository;
import com.sjkb.service.JobService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Note Users must also be contacts, while contacts are not always users
 */
@SessionAttributes({ "currentUser" })
@Controller
@RequestMapping(value = "/backstage/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserContactService userContactService;

    @Autowired
    JobService jobService;


    private String key = null;

    private String getUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        return uname;
    }

    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public String getAllUsers(ModelMap map) {
        
        map.addAttribute("users", userContactService.getAllUser());
        map.addAttribute("user", getUser());
        map.addAttribute("userDelModel", new UserDelModel());
        return "users_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addContact(ModelMap map) {
        UserNewModel user = new UserNewModel();
        key = UUID.randomUUID().toString();
        user.setKey(key);
        map.addAttribute("newuser", user);
        map.addAttribute("user", getUser());
        map.addAttribute("roles", UserRoleModel.getRolesLessThan(getRole()));
        return "users_new";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postContact(ModelMap map, @ModelAttribute("user") final UserNewModel userNewModel) {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (key.equals(userNewModel.getKey()))
            try {
                String jobid = userContactService.addNewUser(userNewModel, uname);
                String contactId = userContactService.getContactByUserid(userNewModel.getUsername()).getUid();
                if (jobid.length() > 3)
                    jobService.createJob(jobid, uname, contactId);
            } catch (UsernameTakenException e) {
                return "users_error_taken";
			}
        
        return getAllUsers(map); 
    }

    @RequestMapping(value = "/deluser/{type}", method = RequestMethod.POST)
    public String delUser(ModelMap map, @ModelAttribute("userDelModel") UserDelModel userDelModel, @PathVariable final String type) {
        userDelModel.setType(type);
        SecurityContext holder = SecurityContextHolder.getContext();
        final String emp = holder.getAuthentication().getName();
        try {
            userContactService.remove(emp, userDelModel);
            return "redirect:/backstage/user/getall";
        } catch (DbxException e) {
            map.addAttribute("errMessage", String.format("<h5>Could not delete Dropbox folder</h5><p class='sjerror'>%s</p>", e.getMessage()));
            return "op_error";
		}
        
    }

    /**
     * 
     * @return the role of the current logged in user
     */

    private String getRole() {
        SecurityContext holder = SecurityContextHolder.getContext();
        Iterator<? extends GrantedAuthority> roles = holder.getAuthentication().getAuthorities().iterator();
        return roles.next().getAuthority();
    }

}