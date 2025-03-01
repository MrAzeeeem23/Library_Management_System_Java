package com.library.library_management.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STAFF")
public class Staff extends User {
    public Staff() {}
    public Staff(String name, String email) {
        super(name, email);
    }

    @Override
    public int getLoanPeriod() {
        return 30;
    }
}