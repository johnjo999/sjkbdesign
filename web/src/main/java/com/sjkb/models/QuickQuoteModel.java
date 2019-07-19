package com.sjkb.models;

import java.util.Date;

public class QuickQuoteModel {

    private String name;
    private Date date;

    private enum Material {
        Granite, Quartiz,
    };

    private Material material;
    private int linearFeet;
    private String islandDimension;
    private int ceiling;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getLinearFeet() {
        return linearFeet;
    }

    public void setLinearFeet(int linearFeet) {
        this.linearFeet = linearFeet;
    }

    public String getIslandDimension() {
        return islandDimension;
    }

    public void setIslandDimension(String islandDimension) {
        this.islandDimension = islandDimension;
    }

    public int getCeiling() {
        return ceiling;
    }

    public void setCeiling(int ceiling) {
        this.ceiling = ceiling;
    }


}