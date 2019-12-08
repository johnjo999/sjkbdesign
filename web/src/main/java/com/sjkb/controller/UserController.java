package com.sjkb.controller;

import java.util.Iterator;
import java.util.UUID;

import com.dropbox.core.DbxException;
import com.sjkb.entities.VendorEntity;
import com.sjkb.exception.UsernameTakenException;
import com.sjkb.models.users.UserDelModel;
import com.sjkb.models.users.UserNewModel;
import com.sjkb.models.users.UserRoleModel;
import com.sjkb.models.users.UserViewModel;
import com.sjkb.models.users.VendorModel;
import com.sjkb.repositores.UserRepository;
import com.sjkb.service.JobService;
import com.sjkb.service.UserContactService;
import com.sjkb.repositores.VendorRepository;

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
import org.springframework.web.bind.annotation.RequestParam;
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
    VendorRepository vendorRepository;

    @Autowired
    UserContactService userContactService;

    @Autowired
    JobService jobService;

    private String key = null;

    private String context = null;;

    public String getUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (context == null) {
            context = userContactService.getContactByUserid(uname).getContext();
            userContactService.setContext(context);
        }
        return uname;
    }

    @RequestMapping(value = "/getalluser", method = RequestMethod.GET)
    public String getAllUsers(ModelMap map) {

        map.addAttribute("users", userContactService.getAllUsers("customer"));
        map.addAttribute("user", getUser());
        UserDelModel userDelModel = new UserDelModel();
        userDelModel.setNextPage("/backstage/user/getalluser");
        map.addAttribute("userDelModel", userDelModel);
        return "contacts/users_list";
    }

    @RequestMapping(value = "/getallrep", method = RequestMethod.GET)
    public String getAllReps(ModelMap map) {

        map.addAttribute("users", userContactService.getAllUsers("salesRep"));
        map.addAttribute("user", getUser());
        UserDelModel userDelModel = new UserDelModel();
        userDelModel.setNextPage("/backstage/user/getallrep");
        map.addAttribute("userDelModel", userDelModel);
        return "contacts/salerep_list";
    }

    @RequestMapping(value = "/add", params = "role", method = RequestMethod.GET)
    public String addRoleContact(ModelMap map, @RequestParam("role") String role) {
        UserNewModel user = new UserNewModel();
        key = UUID.randomUUID().toString();
        user.setKey(key);
        map.addAttribute("newuser", user);
        map.addAttribute("user", getUser());
        map.addAttribute("actrole", role);
        map.addAttribute("action", "/backstage/user/add");
        map.addAttribute("roles", UserRoleModel.getRolesLessThan(getRole()));
        return "contacts/users_new";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addContact(ModelMap map) {
        UserNewModel user = new UserNewModel();
        key = UUID.randomUUID().toString();
        user.setKey(key);
        map.addAttribute("newuser", user);
        map.addAttribute("user", getUser());
        map.addAttribute("actrole", "customer");
        map.addAttribute("action", "/backstage/user/add");
        map.addAttribute("roles", UserRoleModel.getRolesLessThan(getRole()));
        return "contacts/users_new";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postContact(ModelMap map, @ModelAttribute("user") final UserViewModel userModel) {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        String jobid = "";
        if (key.equals(userModel.getKey()))
            try {
                String contactId = userContactService.addNewUser(userModel, uname);
                if (userModel.isNewUser()) {
                    switch (userModel.getRole()) {
                    case "customer":
                        jobid = userContactService.createDbxFolder(userModel, uname);
                        if (jobid.length() > 3) {
                            jobService.createJob(jobid, uname, contactId);
                        }
                        break;
                    case "salesRep":
                        VendorModel vendorModel = new VendorModel();
                        vendorModel.setPocId(contactId);
                        vendorModel.setName(userModel.getCompany());
                        vendorModel.setPostNextAction("/backstage/catalog/newVendor?na=/backstage/user/getallrep");
                        map.addAttribute("vendor", vendorModel);
                        map.addAttribute("user", getUser());
                        return "contacts/create_vendor";

                    }
                }
            } catch (UsernameTakenException e) {
                return "contacts/users_error_taken";
            }

        return getAllUsers(map);
    }

    @RequestMapping(value = "/delete/{type}", method = RequestMethod.POST)
    public String delUser(ModelMap map, @ModelAttribute("userDelModel") UserDelModel userDelModel,
            @PathVariable final String type) {
        userDelModel.setType(type);
        SecurityContext holder = SecurityContextHolder.getContext();
        final String emp = holder.getAuthentication().getName();
        try {
            // return back the page appropriate from where they came
            userContactService.remove(emp, userDelModel);
            return "redirect:" + userDelModel.getNextPage();
        } catch (DbxException e) {
            map.addAttribute("errMessage",
                    String.format("<h5>Could not delete Dropbox folder</h5><p class='sjerror'>%s</p>", e.getMessage()));
            return "op_error";
        }

    }

    @RequestMapping(value = "/edit/{token}", method = RequestMethod.GET)
    public String editUser(ModelMap map, @PathVariable final String token) {
        key = UUID.randomUUID().toString();
        UserViewModel user = userContactService.getByToken(token).setKey(key).setToken(token);
        map.addAttribute("newuser", user);
        map.addAttribute("user", getUser());
        map.addAttribute("actrole", user.getRole());
        map.addAttribute("roles", UserRoleModel.getRolesLessThan(getRole()));
        return "contacts/users_new";
    }

    @RequestMapping(value = "/editcomp/{token}", method = RequestMethod.GET)
    public String editCompany(ModelMap map, @PathVariable final String token) {
        key = UUID.randomUUID().toString();
        UserViewModel userModel = new UserViewModel(userContactService.getContactByUid(token));
        userModel.setKey(key);
        VendorModel vendorModel = new VendorModel();
        VendorEntity entity = vendorRepository.findByRepId(token);
        if (entity == null) {
            vendorModel.setPocId(token);
            vendorModel.setName(userModel.getCompany());
        } else {
            vendorModel = entity.getModel();
        }
        vendorModel.setPostNextAction("/backstage/catalog/newVendor?na=/backstage/user/getallrep");
        map.addAttribute("newuser", userModel);
        map.addAttribute("user", getUser());
        map.addAttribute("vendor", vendorModel);
        return "contacts/edit_vendor";
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