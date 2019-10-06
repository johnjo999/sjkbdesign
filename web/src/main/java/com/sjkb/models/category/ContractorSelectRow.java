package com.sjkb.models.category;

import com.sjkb.entities.ContactEntity;

public class ContractorSelectRow {
    public String name;
    public String id;

    public ContractorSelectRow() {}

    public ContractorSelectRow(ContactEntity contact) {
        this.name = contact.getCompany();
        if (this.name == null || this.name.length() < 2) {
            this.name = contact.getLastname();
        }
        this.id = contact.getUid();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
}