package com.sjkb.service;

import java.util.List;

import com.sjkb.entities.FlexPathEntity;
import com.sjkb.models.category.CategoryModel;
import com.sjkb.models.category.TreePath;
import com.sjkb.models.category.TreeStringModel;

public interface CatalogService {

	List<TreePath> getCatalogForContext(String context, Long pathid);

	List<CategoryModel> getDefaultCatalogTreeForContext(String context);

	List<TreePath> getChildrenOf(FlexPathEntity parent);

	void addCategory(String context, CategoryModel category);

	List<TreeStringModel> getCatalogTreeAsString(String context, long l);
    
}