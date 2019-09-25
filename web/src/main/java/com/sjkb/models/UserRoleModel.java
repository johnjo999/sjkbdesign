package com.sjkb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRoleModel {

    // Admin managed roles must be at the higher ordinals
    public enum ROLES {
        customer, contractor, salesRep, installer, partner, employee, administrator
    };

    public static String getUserRole(String roleString) {
        String result = "";
        ROLES role = ROLES.valueOf(roleString);
        switch (role) {
        case employee:
            result = "ROLE_USER";
            break;
        case administrator:
            result = "ROLE_ADMIN";
            break;
        default:
            result = "ROLE_CUST";
        }
        return result;

    }

	public static List<ROLES> getRolesLessThan(String role) {
        List<ROLES> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(ROLES.values()));
        if (role.equals("ROLE_ADMIN") == false) {
            // from largest ordnal to smallest, in sequence - no gaps
            arrayList.remove(ROLES.administrator.ordinal());
            arrayList.remove(ROLES.employee.ordinal());
            arrayList.remove(ROLES.partner.ordinal());
        }
            
        return arrayList;
	}
}