package com.sjkb.service;

import java.util.ArrayList;
import java.util.List;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.entities.FlexPathEntity;
import com.sjkb.models.category.CategoryModel;
import com.sjkb.models.category.TreePath;
import com.sjkb.models.category.TreeStringModel;
import com.sjkb.repositores.CatalogRepository;
import com.sjkb.repositores.FlexPathRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    FlexPathRepository flexPathRepository;

    @Override
    public List<TreePath> getCatalogForContext(String context, Long pathId) {
        List<FlexPathEntity> roots = flexPathRepository.findByContextAndDepth(context, 0);
        List<TreePath> trees = new ArrayList<>();
        for (FlexPathEntity root: roots) {
            trees.add(new TreePath(root));
        }
        for (TreePath tree : trees) {
            tree.addCatalogItem(catalogRepository.findByDefaultFlexPath(tree.getId()));
        }
        return trees;

    }

    /**
     * Returns a multilevel hash map of the catalog categories
     * A category is a catalogEntity with a ProductID = "category", the level is the members
     * position in the tree, the category of the entity is the parent catagory
     * the root level 0 object have root as their parent
     */

    @Override
    public List<CategoryModel> getDefaultCatalogTreeForContext(String context) {
        List<CategoryModel> stacked = new ArrayList<>();
        List<FlexPathEntity> treeRoots = flexPathRepository.findByContextAndDepth(context, 0);
        for (FlexPathEntity rootEntity : treeRoots) {
            TreePath root = new TreePath(rootEntity);
            root.addChildren(getChildrenOf(rootEntity));
        }
        
        return stacked;
        
    }

    @Override
    public List<TreePath> getChildrenOf(FlexPathEntity parent) {
        List<TreePath> treePath = new ArrayList<>();
        TreePath branch = new TreePath(parent);
        flexPathRepository.findByParent(parent.getIid()).stream().forEach(t -> {
            branch.addChildren(getChildrenOf(t));
        });
        return treePath;
    }

    @Override
    public void addCategory(String context, CategoryModel category) {
        FlexPathEntity flexPath = new FlexPathEntity();
        CatalogEntity catalog = new CatalogEntity();
        if (category.getParent() == 0) {
            flexPath.getFromCategory(category);
            flexPath.setDepth(0);
            flexPath.setContext(context);
            flexPathRepository.save(flexPath);
            catalog.setDefaultFlexPath(flexPath.getIid());
            catalog.setDescription(category.getDescription());
            catalog.setItemId("category");
            catalog.setContext(context);
            catalog.setListLow(category.getLow());
            catalog.setListHigh(category.getHigh());
            catalog.setName(category.getName());
            catalogRepository.save(catalog);
            flexPath.setItemUid(catalog.getUid());
            flexPathRepository.save(flexPath);
        }


    }

    @Override
    public List<TreeStringModel> getCatalogTreeAsString(String context, long l) {
        List<TreeStringModel> result = new ArrayList<>();
        List<TreePath> forest = getTreePathsForContext(context);
        for (TreePath tree : forest) {
            TreeStringModel stringModel = new TreeStringModel();
            stringModel.setName(tree.getName());
            result.add(stringModel);
        }
        return result;
    }

    private List<TreePath> getTreePathsForContext(String context) {
        List<FlexPathEntity> roots = flexPathRepository.findByContextAndDepth(context, 0);
        List<TreePath> result = new ArrayList<>();
        for (FlexPathEntity tree : roots) {
            TreePath path = new TreePath(tree);
            result.add(path);
        }
        return result;
    }

  
}