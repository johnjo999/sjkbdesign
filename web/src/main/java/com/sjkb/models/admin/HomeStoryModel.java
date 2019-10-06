package com.sjkb.models.admin;

import java.util.ArrayList;
import java.util.List;

public class HomeStoryModel {
    List<String> t = new ArrayList<>();
    String homeFont;
    String oldFont;

    public List<String> getT() {
        return t;
    }

    public void setT(List<String> t) {
        this.t = t;
    }

    public String getHomeFont() {
        return homeFont;
    }

    public void setHomeFont(String homeFont) {
        this.homeFont = homeFont;
    }

    public String getOldFont() {
        return oldFont;
    }

    public void setOldFont(String oldFont) {
        this.oldFont = oldFont;
    }

    
    

}