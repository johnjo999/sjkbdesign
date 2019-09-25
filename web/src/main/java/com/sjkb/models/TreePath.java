package com.sjkb.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreePath {
    private Map<String, TreePath> branch = new HashMap<>();

    public void addBranch(String name) {
        branch.put(name, new TreePath());
    }

    public boolean branchHas(String name) {
        boolean result = false;
        if (branch.containsKey(name))
            return true;
        else {
            for (String path : branch.keySet()) {
                result = result | branch.get(path).branchHas(name);
                if (result)
                    break;
            }
        }
        return result;
    }

    public TreePath getBranch(String name) {
        TreePath result = null;
        if (branch.containsKey(name))
            return branch.get(name);
        else {
            for (String path : branch.keySet()) {
                result = branch.get(path).getBranch(name);
                if (result != null)
                    break;
            }
        }
        return result;
    }


    public List<CategoryModel> getFlatList() {
        List<CategoryModel> result = new ArrayList<>();
        for (String path : branch.keySet()) {
            result.addAll(branch.get(path).getFlatList());
        }
        return result;
    }
}