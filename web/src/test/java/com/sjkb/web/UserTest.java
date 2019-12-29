package com.sjkb.web;

import static org.junit.Assert.assertEquals;

import com.sjkb.models.users.VendorModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class UserTest {

    @Test
    public void testPwdCrypto() {
        VendorModel vendor = new VendorModel();
        vendor.setPwd("testing");
        String crypt = vendor.getPwdEncrypt();
        vendor.setPwd(crypt);
        assertEquals(vendor.getPwdDecrypt(), "testing");
        
    }
    

}
