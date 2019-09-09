package com.sjkb.controller;

import com.sjkb.components.Material;
import com.sjkb.models.BathCardModel;
import com.sjkb.models.CardModel;
import com.sjkb.models.KitchenCardModel;
import com.sjkb.models.QuickQuoteModel;
import com.sjkb.service.JobService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/backstage")
public class BackstageController {

    @Autowired
    JobService jobService;

    @Autowired
    UserContactService userContactService;

    private String context = null;

    private String getUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        if (context == null) {
            context = userContactService.getContactByUserid(uname).getContext();
            userContactService.setContext(context);
        }
        return uname;
    }

    @RequestMapping(value = "dashboard")
    public String dashboard(ModelMap map) {
        map.addAttribute("user", getUser());
        return "dashboard";
    }

    @RequestMapping(value = "doquote")
    public String doQuote(ModelMap map) {
        map.addAttribute("quote", new QuickQuoteModel());
        map.addAttribute("mats", Material.getList());
        map.addAttribute("user", getUser());
        return "doquickquote";
    }

    @RequestMapping(value = "getquotecard")
    public String getQuoteCard(ModelMap map, @RequestParam("card") final String card) {
        String viewModel = null;
        CardModel cardModel = null;
        switch (card) {
        case "kitchen":
            cardModel = new KitchenCardModel();
            viewModel = "fragments/cards::kitchen";
            break;
        case "bath":
            viewModel = "fragments/cards::bath";
            cardModel = new BathCardModel();
            break;
        default:
            viewModel = "fragments/cards::generic";
            cardModel = new CardModel();
            cardModel.setTitle(card);

        }
        map.addAttribute("mats", Material.getList());
        map.addAttribute("cardModel", cardModel);
        map.addAttribute("user", getUser());
        return viewModel;
    }

    /**
     * Add room card to quick quote
     */
    @RequestMapping(value = "addroomtoqq", method = RequestMethod.POST)
    public String addRoomToQuickQuote(ModelMap map, @ModelAttribute("cardModel") final CardModel cardModel) {
        return "fragments/cards::qqrow";
    }

    @RequestMapping(value = "get/jobs/{type}")
    public String getJobs(ModelMap map, @PathVariable("type") final String type) {
        SecurityContext holder = SecurityContextHolder.getContext();
        final String uname = holder.getAuthentication().getName();
        map.addAttribute("cards", jobService.getAllJobsForUser(uname));
        return "jobs_list";
    }

}