package com.sjkb.models.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjkb.entities.CatalogEntity;
import com.sjkb.entities.FlexPathEntity;

public class TreePath {

    public TreePath(FlexPathEntity flex) {
        this.flexEntity = flex;
    }

    private Map<Long, TreePath> children = new HashMap<>();

    private FlexPathEntity flexEntity;
    private List<CatalogEntity> catalogItems;

    public TreePath addBranch(FlexPathEntity flex) {
        TreePath newBranch = new TreePath(flex);
        flex.setParent(flexEntity.getIid());
        children.put(flex.getIid(), newBranch);
        return newBranch;
    }

    public boolean hasBranch(Long id) {
        boolean result = id == this.getId();
        if (children.containsKey(id))
            return true;
        else {
            for (Long childId : children.keySet()) {
                result = result | children.get(childId).hasBranch(id);
                if (result)
                    break;
            }
        }
        return result;
    }

    public TreePath getChildren(Long id) {
        TreePath result = null;
        if (children.containsKey(id))
            return children.get(id);
        else {
            for (Long path : children.keySet()) {
                result = children.get(path).getChildren(id);
                if (result != null)
                    break;
            }
        }
        return result;
    }


    public List<CategoryModel> getFlatList() {
        List<CategoryModel> result = new ArrayList<>();
        for (Long id : children.keySet()) {
            result.addAll(children.get(id).getFlatList());
        }
        return result;
    }

	public Long getId() {
        return flexEntity.getIid();
    }
    
    public Long getParent() {
        return flexEntity.getParent();
    }

	public void addChildren(List<TreePath> childrenOf) {
        for (TreePath child : childrenOf) {
            children.put(child.getId(), child);
        }
	}

	public TreePath getBranch(FlexPathEntity pathEntity) {
		return getChildren(pathEntity.getIid());
	}

	public boolean hasBranch(FlexPathEntity pathEntity) {
		return hasBranch(pathEntity.getIid());
	}

	public TreePath getChildren(FlexPathEntity e3) {
		return getChildren(e3.getIid());
	}

	public void addCatalogItem(CatalogEntity catalogEntity) {
        if (catalogItems == null)
            catalogItems = new ArrayList<>();
        catalogItems.add(catalogEntity);
    }

    public FlexPathEntity getFlexEntity() {
        return flexEntity;
    }

    public void setFlexEntity(FlexPathEntity flexEntity) {
        this.flexEntity = flexEntity;
    }

    public List<CatalogEntity> getCatalogItems() {
        return catalogItems;
    }

    public String getName() {
        if (flexEntity == null || flexEntity.getName() == null)
            return "annoymous";
        return flexEntity.getName();
    }
    
    
}