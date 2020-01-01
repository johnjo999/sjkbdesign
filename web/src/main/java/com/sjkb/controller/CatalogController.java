package com.sjkb.controller;

import java.util.List;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.VendorEntity;
import com.sjkb.models.category.CategoryModel;
import com.sjkb.models.category.HeritageModel;
import com.sjkb.models.category.NewItemModel;
import com.sjkb.models.category.TreePath;
import com.sjkb.models.users.VendorModel;
import com.sjkb.repositores.ContactRepository;
import com.sjkb.repositores.VendorRepository;
import com.sjkb.service.CatalogService;
import com.sjkb.service.UserContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/backstage/catalog")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    UserContactService userService;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    ContactRepository contactRepository;

    @RequestMapping(value = "getall")
    public String getAllComponents(ModelMap map) {
        String context = userService.getContext();
        List<TreePath> catalog = catalogService.getCatalogForContext(context, 0l);
        map.addAttribute("catalog", catalog);
        return "catalog/catalog_listing";
    }

    @RequestMapping(value = "addcat", method = RequestMethod.GET)
    public String getCatagory(ModelMap map) {
        String context = userService.getContext();
        CategoryModel cat = new CategoryModel();
        List<CategoryModel> categoryTree = catalogService.getDefaultCatalogTreeForContext(context);
        map.addAttribute("cat", cat);
        map.addAttribute("tree", categoryTree);
        map.addAttribute("trees", catalogService.getCatalogTreeAsString(context, 0l));
        return "catalog/catalog_new";

    }

    @RequestMapping(value = "addcat", method = RequestMethod.POST)
    public String postCatagory(ModelMap map, @ModelAttribute("cat") final CategoryModel category) {
        if (category.getName().length() > 0) {
            catalogService.addCategory(userService.getContext(), category);
            return getChildrenOf(map, String.valueOf(category.getParent()));
        }
        return "redirect:/backstage/catalog/addcat";

    }

    @RequestMapping(value = "additem", method = RequestMethod.POST)
    public String postItem(ModelMap map, @ModelAttribute("item") final CategoryModel category) {
        catalogService.addCategory(userService.getContext(), category);
        return getChildrenOf(map, String.valueOf(category.getParent()));

    }

    @RequestMapping(value = "getChildren/{id}", method = RequestMethod.GET)
    public String getChildrenOf(ModelMap map, @PathVariable("id") final String id) {
        Long iid = 0l;
        try {
            iid = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            return "error";
        }
        HeritageModel heritage = catalogService.getHeritage(iid);
        if (heritage.getMyself() == null)
            return "redirect:/backstage/catalog/addcat";
        map.addAttribute("heritage", heritage);
        return "fragments/catalog::childrow";
    }

    @RequestMapping(value = "getChildrenEditable/{id}", method = RequestMethod.GET)
    public String getChildrenEditableOf(ModelMap map, @PathVariable("id") final String id) {
        Long iid = 0l;
        try {
            iid = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            return "error";
        }
        HeritageModel heritage = catalogService.getHeritage(iid);
        map.addAttribute("heritage", heritage);
        return "fragments/catalog::childrowbutton";
    }

    @RequestMapping(value = "additem", method = RequestMethod.GET)
    public String getNewItem(ModelMap map) {
        NewItemModel item = new NewItemModel();
        String context = userService.getContext();
        List<CategoryModel> categoryTree = catalogService.getDefaultCatalogTreeForContext(context);
        map.addAttribute("item", item);
        map.addAttribute("tree", categoryTree);
        map.addAttribute("trees", catalogService.getCatalogTreeAsString(context, 0l));
        return "catalog/item_new";

    }

    @RequestMapping(value = "newVendor", method = RequestMethod.POST, params = "na")
    public String postNewVendor(ModelMap map, @RequestParam("na") final String na,
            @ModelAttribute("vendor") final VendorModel vendor) {
        VendorEntity vendorEntity = vendorRepository.findByName(vendor.getName());
        if (vendorEntity == null)
            vendorEntity = new VendorEntity();
        // ensure sales rep is for this company. 
        ContactEntity salesRep = contactRepository.getOne(vendor.getPocId());
        if (salesRep.getCompany().equals(vendor.getName()) == false) {
            salesRep.setCompany(vendor.getName());
            contactRepository.save(salesRep);
        }
        vendorEntity.parseModel(vendor);
        vendorEntity.setContext(userService.getContext());
        vendorRepository.save(vendorEntity);
        CatalogEntity catalog = new CatalogEntity();
        catalog.setContext(userService.getContext());
        catalog.setName(vendor.getName());
        catalog.setVendorId(vendorEntity.getUid());
        catalogService.addItemTo("/Vendor", catalog);
        return "redirect:" + na;

    }

}