package com.sjkb.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "catalog")
@Table(name = "catalog")
    
public class CatalogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;

    private String context;
    private String name;
    private String itemId;
    private String description;
    private int listLow;
    private int listHigh;
    private String vendorId;
    private String url;
    private Long defaultFlexPath;

    

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getListLow() {
        return listLow;
    }

    public void setListLow(int listLow) {
        this.listLow = listLow;
    }

    public int getListHigh() {
        return listHigh;
    }

    public void setListHigh(int listHigh) {
        this.listHigh = listHigh;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDefaultFlexPath() {
        return defaultFlexPath;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setDefaultFlexPath(Long defaultFlexPath) {
        this.defaultFlexPath = defaultFlexPath;
    }

}