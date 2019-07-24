package com.sjkb.controller;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.UserEntity;
import com.sjkb.models.UserNewModel;
import com.sjkb.models.UserRoleModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
    ContactRepository contactRepository;

    private static final Logger log = Logger.getLogger(Class.class.getName());

    private String key = null;

    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public String getAllUsers(ModelMap map) {
        map.addAttribute("users", contactRepository.findAll());
        return "users_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addContact(ModelMap map) {
        UserNewModel user = new UserNewModel();
        key = UUID.randomUUID().toString();
        user.setKey(key);
        map.addAttribute("user", user);
        map.addAttribute("roles", UserRoleModel.ROLES.values());
        return "users_new";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postContact(ModelMap map, @ModelAttribute("user") final UserNewModel userNewModel) {
        if (key.equals(userNewModel.getKey())) {
            if (userNewModel.getUsername().length() == 0 && userNewModel.getEmail().length() > 3) {
                userNewModel.setUsername(userNewModel.getEmail());
            }
            if (userNewModel.getUsername().length() > 2) {
                Optional<UserEntity> user = userRepository.findByUsername(userNewModel.getUsername());
                if (user.isPresent() == true) {
                    return "users_error_taken";
                }
                if (userNewModel.getPassword().length() == 0) {
                    int i = userNewModel.getUsername().length();
                    int j = userNewModel.getPhone().length();
                    if (i > 4)
                        i = 4;
                    if (j < 4)
                        j = 4;
                    String fn = userNewModel.getFirstname().substring(0, i).toLowerCase();
                    String ph = userNewModel.getPhone().substring(j - 4);
                    userNewModel.setPassword(fn+ph);                  
                }
                if (userNewModel.getPassword().length() > 2) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.encodePwd(userNewModel.getPassword());
                    userEntity.setUsername(userNewModel.getUsername());
                    userEntity.setLocked(false);
                    userEntity.setRole(UserRoleModel.getUserRole(userNewModel.getRole()));
                    userRepository.save(userEntity);
                }
            }
            ContactEntity contactEntity = new ContactEntity(userNewModel);
            contactRepository.save(contactEntity);

        }
        return getAllUsers(map); 
    }

    @RequestMapping(value = "newuser/{userid}/{pwd}")
    public String newlogint(@PathVariable("userid") String userid, @PathVariable("pwd") String pwd) {
        UserEntity user = new UserEntity();
        user.encodePwd(pwd);
        user.setUsername(userid);
        userRepository.save(user);
        return "home";
    }

}