package com.sjkb.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sjkb.entities.FlexPathEntity;
import com.sjkb.models.category.TreePath;
import com.sjkb.service.CatalogService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest

public class FlexPathTest {

    @Autowired
    CatalogService catalogService;


    @Test
    public void testTreePath() {
        
        FlexPathEntity e1 = new FlexPathEntity();
        e1.test();
        e1.setName("branchA1");
        FlexPathEntity e2 = new FlexPathEntity();
        e2.test();
        e2.setName("branchA2");
        FlexPathEntity e3 = new FlexPathEntity();
        e3.setName("branchB1");
        e3.test();
        TreePath tree = new TreePath(e1);
        tree.addBranch(e3);
        TreePath t3 = tree.getBranch(e3).addBranch(e2);
        assertTrue("branch contains", tree.hasBranch(e1));
        assertFalse("branch does not contains", tree.hasBranch(44L));
        assertTrue("branch has", tree.hasBranch(e2));
        assertFalse("branch not has", t3.hasBranch(e1));
    }

}
