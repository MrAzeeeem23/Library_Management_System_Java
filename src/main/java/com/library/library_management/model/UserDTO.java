package com.library.library_management.model;

public class UserDTO {
    private String name;
    private String email;
    private String userType;
    private String password; // plain password

    public UserDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}