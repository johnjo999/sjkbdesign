package com.sjkb.models.category;

import java.util.List;


/**
 * Covers direct decendants (children), and the path to root
 */
public class HeritageModel {
    private List<TreePath> heritage;
    private TreePath myself;
    
    public List<TreePath> getHeritage() {
        return heritage;
    }

    public void setHeritage(List<TreePath> heritage) {
        this.heritage = heritage;
    }

    public TreePath getMyself() {
        return myself;
    }

    public void setMyself(TreePath myself) {
        this.myself = myself;
    }

    
    
}