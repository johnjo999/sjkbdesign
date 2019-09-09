package com.sjkb.controller;


import com.sjkb.service.CatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/backstage/componet")
public class CatalogController {

    @Autowired
    CatalogService componentService;

    @RequestMapping(value = "getall")
    public String getAllComponents(ModelMap map) {
       // List<ComponentGroupEntity> groups = new ArrayList<>();
        
       return ""; 
    }
}