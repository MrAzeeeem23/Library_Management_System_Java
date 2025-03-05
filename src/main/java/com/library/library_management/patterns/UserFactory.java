package com.library.library_management.patterns;

import com.library.library_management.model.Patron;
import com.library.library_management.model.Staff;
import com.library.library_management.model.User;


public class UserFactory {
    public static User createUser(String type, int id, String name, String email, String password) {
        switch (type.toLowerCase()) {
            case "patron":
                return new Patron(name, email, password);
            case "staff":
                return new Staff(name, email, password);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}