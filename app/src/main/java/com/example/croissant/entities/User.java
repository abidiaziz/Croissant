package com.example.croissant.entities;

public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private boolean isAdmin;
    private boolean isActive;
    private String photoUrl;

    public User() {

    }

    public User(String id, String email, String firstName, String lastName, int age, boolean isAdmin, boolean isActive, String photoUrl) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
        this.photoUrl = photoUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean active) { isActive = active; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}

