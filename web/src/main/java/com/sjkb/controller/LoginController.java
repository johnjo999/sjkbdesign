package com.sjkb.controller;

import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.sjkb.components.UserComponent;
import com.sjkb.entities.FontFamilyEntity;
import com.sjkb.entities.HomePageStoryEntity;
import com.sjkb.models.admin.HomeStoryModel;
import com.sjkb.repositores.FontRepository;
import com.sjkb.repositores.HomeStoryRepository;
import com.sjkb.repositores.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@SessionAttributes({ "currentUser" })
@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HomeStoryRepository homeStoryRepository;

    @Autowired
    FontRepository fontRepository;

    private static final Logger log = Logger.getLogger(Class.class.getName());

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String testlogin() {
        return "dashboard";
    }

    @RequestMapping(value = "/")
    public String homeroot(@RequestHeader Map<String, String> header) {
        return "redirect:/home.html";
    }

    @RequestMapping(value = "/home")
    public String homelogint(ModelMap map) {
        List<FontFamilyEntity> font = fontRepository.findByHomepageTrue();
        if (font.size() > 0) {
            map.addAttribute("homefont", font.get(0));
            map.addAttribute("homefontlink", font.get(0).getFamily().replaceAll(" ", "+"));
        }
        List<HomePageStoryEntity> stories = homeStoryRepository.findAll();
        HomeStoryModel storyModel = new HomeStoryModel();
        int x = 1;
        List<String> t = storyModel.getT();
        t.add("NA");
        for (HomePageStoryEntity story : stories) {
            t.add(x++, story.getStory());
        }
        for (; x < 30; x++) {
            t.add(x, "");
        }
        map.addAttribute("story", storyModel);
        return "home";
    }

    @RequestMapping(value = "/headers")
    public String showheaders(ModelMap map, @RequestHeader("X-Forwarded-Proto") String proto,
            @RequestHeader Map<String, String> headers) {

        map.addAttribute("proto", proto);
        map.addAttribute("all", headers);
        return "headers";
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
            throw new IllegalArgumentException("Principal can not be null!");
        }
    }
}