package com.sjkb.controller;

import java.util.List;

import com.sjkb.models.category.CategoryModel;
import com.sjkb.models.category.TreePath;
import com.sjkb.service.CatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        if (context == null) {
            context = backstageController.getContext();
        }
        List<TreePath> catalog = catalogService.getCatalogForContext(context, 0l);
        map.addAttribute("catalog", catalog);
        return "catalog/catalog_listing";
    }

    @RequestMapping(value = "addcat", method = RequestMethod.GET)
    public String getCatagory(ModelMap map) {
        if (context == null) {
            context = backstageController.getContext();
        }
        CategoryModel cat = new CategoryModel();
        List<CategoryModel> categoryTree = catalogService.getDefaultCatalogTreeForContext(context);
        map.addAttribute("cat", cat);
        map.addAttribute("tree", categoryTree);
        map.addAttribute("trees", catalogService.getCatalogTreeAsString(context, 0l));
        return "catalog/catalog_new";

    }

    @RequestMapping(value = "addcat", method = RequestMethod.POST)
    public String postCatagory(ModelMap map, @ModelAttribute("cat") final CategoryModel category) {
        catalogService.addCategory(context, category);
        return "redirect:/backstage/catalog/getall";

    }

}