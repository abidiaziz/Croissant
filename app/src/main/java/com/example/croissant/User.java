package com.example.croissant;

public class User {
    private String id;
    private String email;
    private boolean isAdmin;
    private boolean isActive;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String id, String email, boolean isAdmin, boolean isActive) {
        this.id = id;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}

