package com.sjkb.models;

public class UserRoleModel {

    public enum ROLES {
        customer, contractor, vendor, installer, partner, employee
    };

    public static String getUserRole(String roleString) {
        String result = "";
        ROLES role = ROLES.valueOf(roleString);
        switch (role) {
        case employee:
            result = "ROLE_USER";
            break;
        default:
            result = "ROLE_CUST";
        }
        return result;

    }
}