package com.sjkb.service;

import java.util.List;
import java.util.Map;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.models.CategoryModel;

public interface CatalogService {

	Map<String, List<CatalogEntity>> getCatalogForContext(String context);

	List<CategoryModel> getCatalogTreeForContext(String context);
    
}