package com.sjkb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.models.CategoryModel;
import com.sjkb.models.TreePath;
import com.sjkb.repositores.CatalogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Map<String, List<CatalogEntity>> getCatalogForContext(String context) {
        Map<String, List<CatalogEntity>> catalog = new HashMap<>();
        List<CatalogEntity> all = catalogRepository.findByContext(context);
        for (CatalogEntity item : all) {
            if (catalog.containsKey(item.getCategory()) == false) {
                catalog.put(item.getCategory(), new ArrayList<CatalogEntity>());
            }
            catalog.get(item.getCategory()).add(item);
        }
        return catalog;

    }
    /**
     * Returns a multilevel hash map of the catalog categories
     * A category is a catalogEntity with a ProductID = "category", the level is the members
     * position in the tree, the category of the entity is the parent catagory
     * the root level 0 object have root as their parent
     */

    @Override
    public List<CategoryModel> getCatalogTreeForContext(String context) {
        List<CatalogEntity> members = catalogRepository.findByContextAndItemId(context, "category");
        TreePath tree = new TreePath();
        tree.addBranch("root");

        // sort by level to ensure we craate folders top down
        TreePath parent = tree.getBranch("root");
        Collections.sort(members, CatalogEntity.OrderByLevel);
        for (CatalogEntity member : members) {
            parent = parent.getBranch(member.getCategory());
            if (parent != null) {
                parent.addBranch(member.getName());
            } else {
                tree.addBranch(member.getName());
            }
        }
        return tree.getFlatList();
        
    }

}