package com.sjkb.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.sjkb.components.UserComponent;
import com.sjkb.entities.UserEntity;
import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@SessionAttributes({ "currentUser" })
@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    private static final Logger log = Logger.getLogger(Class.class.getName());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String testlogin() {
        return "dashboard";
    }

    @RequestMapping(value = "/home")
    public String homelogint() {
        return "home";
    }

    @RequestMapping(value = "newuser/{userid}/{pwd}")
    public String newlogint(@PathVariable("userid") String userid, @PathVariable("pwd") String pwd) {
        UserEntity user = new UserEntity();
        user.encodePwd(pwd);
        user.setUsername(userid);
        userRepository.save(user);
        return "home";
    }



    @RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
    public String loginError(Model model) {
        log.info("Login attempt failed");
        model.addAttribute("error", "true");
        return "login";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model) {
        log.info("Login attempt failed");
        model.addAttribute("error", "true");
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "logout_success";
    }

    @RequestMapping(value = "/postLogin", method = RequestMethod.POST)
    public String postLogin(Model model, HttpSession session) {
        log.info("postLogin()");
        // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        User loggedInUser = ((UserComponent) authentication.getPrincipal()).getUserDetails();
        model.addAttribute("currentUser", ((UserDetails) loggedInUser).getUsername());
        session.setAttribute("userId", loggedInUser.getName());
        return "redirect:/wallPage";
    }
    private void validatePrinciple(Object principal) {
        if (!(principal instanceof UserComponent)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }
}