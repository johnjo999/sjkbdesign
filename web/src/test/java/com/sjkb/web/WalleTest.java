package com.sjkb.web;

import com.sjkb.service.Walle;
import com.sjkb.service.WalleAsync;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalleTest {

    @Autowired
    Walle walle;

    @Autowired
    WalleAsync walleAsync;

    @Test
    public void testGetMessage() throws InterruptedException {
     /*   while (walle.waitForSub()) {
            Thread.sleep(1000);
            System.out.print("!");
        }
        walleAsync.getMessageId("Y2lzY29zcGFyazovL3VzL01FU1NBR0UvN2Q1ZjkyODAtZGE2Yy0xMWVhLTliZmMtNWZmNWFmOTI0ZjA3");
        while (true) {
            Thread.sleep(1000);
            System.out.print(".");
        }  */
    } 
    
}