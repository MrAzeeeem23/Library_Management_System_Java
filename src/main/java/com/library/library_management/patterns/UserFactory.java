package com.library.library_management.patterns;

import com.library.library_management.model.Patron;
import com.library.library_management.model.Staff;
import com.library.library_management.model.User;

public class UserFactory {
    public static User createUser(String type, int id, String name, String email) {
        switch (type.toLowerCase()) {
            case "patron":
                return new Patron(name, email);  // Pass name and email directly
            case "staff":
                return new Staff(name, email);   // Pass name and email directly
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}