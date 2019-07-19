package com.sjkb.controller;

import com.sjkb.components.Material;
import com.sjkb.models.BathCardModel;
import com.sjkb.models.CardModel;
import com.sjkb.models.KitchenCardModel;
import com.sjkb.models.QuickQuoteModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/backstage")
public class BackstageController {

    @RequestMapping(value = "dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @RequestMapping(value = "doquote")
    public String doQuote(ModelMap map) {
        map.addAttribute("quote", new QuickQuoteModel());
        map.addAttribute("mats", Material.getList());
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
        return viewModel;
    }

    /**
     * Add room card to quick quote
     */
    @RequestMapping(value = "addroomtoqq", method = RequestMethod.POST)
    public String addRoomToQuickQuote(ModelMap map, @ModelAttribute("cardModel") final CardModel cardModel) {
        return "fragments/cards::qqrow";
    }
}