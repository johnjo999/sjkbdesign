package com.sjkb.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "font")
@Table(name = "fonts")
public class FontFamilyEntity {

    @Id
    private String family;

    private boolean homepage;

    private long pageflags;

    private String type;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public boolean isHomepage() {
        return homepage;
    }

    public void setHomepage(boolean homepage) {
        this.homepage = homepage;
    }

    public long getPageflags() {
        return pageflags;
    }

    public void setPageflags(long pageflags) {
        this.pageflags = pageflags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    

}