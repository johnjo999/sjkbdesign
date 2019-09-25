package com.sjkb.controller;

import java.util.List;
import java.util.Map;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.models.CategoryModel;
import com.sjkb.service.CatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/backstage/catalog")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    BackstageController backstageController;

    private String context;

    @RequestMapping(value = "getall")
    public String getAllComponents(ModelMap map) {
       // List<ComponentGroupEntity> groups = new ArrayList<>();
        if (context == null) {
            context = backstageController.getContext();
        }
        Map<String, List<CatalogEntity>> catalog = catalogService.getCatalogForContext(context);
        map.addAttribute("catalog", catalog);
       return "catalog_listing"; 
    }

    @RequestMapping(value = "addcat", method = RequestMethod.GET)
    public String getCatagory(ModelMap map) {
        CategoryModel cat = new CategoryModel();
        List<CategoryModel> categoryTree = catalogService.getCatalogTreeForContext(context);
        map.addAttribute("cat", cat);
        map.addAttribute("tree", categoryTree);
        return "catalog_addcat";

    }
        
}