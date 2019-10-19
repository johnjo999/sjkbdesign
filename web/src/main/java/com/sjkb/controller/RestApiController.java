package com.sjkb.controller;

import com.sjkb.models.category.HeritageModel;
import com.sjkb.models.category.TreePath;
import com.sjkb.service.CatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/backstage/rest")
public class RestApiController {

    @Autowired
    CatalogService catalogService;

    @RequestMapping(value = "flexpath/children/{id}")
    public TreePath getChildrenOfPath(@PathVariable final String id) {
        Long iid = 0l;
        try {
            iid = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            return null;
        }
        return catalogService.getChildrenOfPath(iid);
        
    }

    @RequestMapping(value = "flexpath/heritage/{id}")
    public HeritageModel getHeritage(@PathVariable final String id) {
        Long iid = 0l;
        try {
            iid = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            return null;
        }
        return catalogService.getHeritage(iid);
    }

}