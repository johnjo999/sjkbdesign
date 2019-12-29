package com.sjkb.controller;

import java.util.List;

import com.sjkb.entities.DropboxTokenEntity;
import com.sjkb.entities.FontFamilyEntity;
import com.sjkb.entities.HomePageStoryEntity;
import com.sjkb.models.admin.DropboxReturnToken;
import com.sjkb.models.admin.DropboxTokenModel;
import com.sjkb.models.admin.HomeStoryModel;
import com.sjkb.repositores.FontRepository;
import com.sjkb.repositores.HomeStoryRepository;
import com.sjkb.service.DropboxService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    DropboxService dropboxService;

    @Autowired
    HomeStoryRepository homeStoryRepository;

    @Autowired
    FontRepository fontRepository;

    @Autowired
    UserContactService userService;

    @RequestMapping(value = "setup/dropbox")
    public String showDropboxTokens(ModelMap map) {

        List<DropboxTokenModel> tokens = dropboxService.getAllUserTokens(userService.getContext());
        for (DropboxTokenModel token: tokens) {
            token.setUser(userService.getUsernameFor(token.getUser()));
        }
        map.addAttribute("tokens", tokens);
        map.addAttribute("user", userService.getUserIs());
        return "admin/setup_dropbox";
    }

    @RequestMapping(value = "grabtoken")
    public String getDropboxToken(ModelMap mmap, @RequestParam("state") final String state,
            @RequestParam("code") final String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", "https://www.sjkbdesign.com/admin/grabtoken");
        map.add("client_id", "00muxcqeqn6dtr3");
        map.add("client_secret", "quhhdlv7044vzfh");
        String url = "https://api.dropbox.com/oauth2/token";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<DropboxReturnToken> response = restTemplate.postForEntity(url, request,
                DropboxReturnToken.class);
        DropboxTokenEntity token = new DropboxTokenEntity(response);
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        token.setUser(uname);
        token.setContext(userService.getContext());
        dropboxService.saveToken(token);
        return "redirect:/admin/setup/dropbox";

    }

    @RequestMapping(value = "revokeDbToken/{user}")
    public String getDropboxToken(ModelMap map, @PathVariable("user") final String user) {
        return "redirect:/admin/setup/dropbox";
    }

    @RequestMapping(value = "setup/home", method = RequestMethod.GET)
    public String updateHomeGet(ModelMap map) {
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
        List<FontFamilyEntity> oldFont = fontRepository.findByHomepageTrue();
        List<FontFamilyEntity> availableFonts = fontRepository.findAll();
        FontFamilyEntity defaultFont = new FontFamilyEntity();
        defaultFont.setFamily("default");
        availableFonts.add(0, defaultFont);
        if (oldFont.size() > 0) {
            storyModel.setOldFont(oldFont.get(0).getFamily());
        }
        map.addAttribute("story", storyModel);
        map.addAttribute("fonts", availableFonts);
        return "admin/home_markup";
    }

    @RequestMapping(value = "setup/homeupdate", method = RequestMethod.POST)
    public String updateHomePut(ModelMap map, @ModelAttribute("story") final HomeStoryModel storyModel) {

        if (storyModel.getHomeFont() != null) {
            if (storyModel.getHomeFont().equals("default")) {
                // setting to default
                if (storyModel.getOldFont() != null) {
                    FontFamilyEntity old = fontRepository.getOne(storyModel.getOldFont());
                    old.setHomepage(false);
                    fontRepository.save(old);
                }
            } else if (storyModel.getOldFont() != null) {
                if (storyModel.getOldFont().equals(storyModel.getHomeFont()) == false) {
                    if (fontRepository.existsById(storyModel.getOldFont())) {
                        FontFamilyEntity old = fontRepository.getOne(storyModel.getOldFont());
                        old.setHomepage(false);
                        fontRepository.save(old);
                    }
                    if (fontRepository.existsById(storyModel.getHomeFont())) {
                        FontFamilyEntity current = fontRepository.getOne(storyModel.getHomeFont());
                        current.setHomepage(true);
                        fontRepository.save(current);
                    }
                }
            } else {
                FontFamilyEntity current = fontRepository.getOne(storyModel.getHomeFont());
                current.setHomepage(true);
                fontRepository.save(current);
            }
        }

        List<HomePageStoryEntity> stories = homeStoryRepository.findAll();
        int x = 0;
        storyModel.getT().remove(0);
        for (String story : storyModel.getT()) {
            if (story == null)
                story = "";
            HomePageStoryEntity entity = null;
            if (x < stories.size())
                entity = stories.get(x++);
            else {
                entity = new HomePageStoryEntity();
                entity.setId((long) ++x);
            }
            entity.setStory(story);
            homeStoryRepository.save(entity);
        }
        return "redirect:/admin/setup/home";
    }

}