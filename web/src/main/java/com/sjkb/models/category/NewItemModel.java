package com.sjkb.models.category;

import java.util.ArrayList;
import java.util.List;

public class NewItemModel {
    private String name;
    private String description;
    private int high;
    private int low;
    private List<Long> flexPaths;

    public NewItemModel() {
        this.flexPaths = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public List<Long> getFlexPaths() {
        return flexPaths;
    }

    public void setFlexPaths(List<Long> flexPaths) {
        this.flexPaths = flexPaths;
    }

    public Long getParent() {
        if (flexPaths.size() > 0) {
            return flexPaths.get(0);
        }
        return 0L;
    }
 
    

}