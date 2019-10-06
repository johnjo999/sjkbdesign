package com.sjkb.entities;

import java.util.Comparator;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjkb.models.category.CategoryModel;

import org.springframework.data.annotation.Transient;



@Entity(name = "flexpath")
@Table(name = "flexpath")
public class FlexPathEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;
    
    // path to root, hex of ancestor iid (in hex) with - between pathids, ie 1-a-b indicates a is root, c is parent
    private Long parent;
    
    // name of this foler, branch
    private String name;
    
    // the particular object on this branch
    private Long itemUid;
    
    private String context;
    
    // hops to root, with root at 0
    private int depth;

    public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getItemUid() {
        return itemUid;
    }

    public void setItemUid(Long itemUid) {
        this.itemUid = itemUid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    @Transient
    public void test() {
        iid = new Random().nextLong();
    }

	public void getFromCategory(CategoryModel category) {
        this.name = category.getName();
        this.depth = category.getLevel();
        this.parent = (long) category.getParent();
    }
    
    static public final Comparator<FlexPathEntity> OrderByLevel = new Comparator<FlexPathEntity>() {

        @Override
        public int compare(FlexPathEntity o1, FlexPathEntity o2) {
            if (o1.getDepth() > o2.getDepth())
                return 1;
            if (o1.getDepth() < o2.getDepth())
                return -1;
            return 0;
        }

    };

}