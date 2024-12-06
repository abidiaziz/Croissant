package com.example.croissant.entities;

public class UserComparison {
    private String userId;
    private String firstName;
    private String lastName;
    private double matchPercentage;

    public UserComparison(String userId, String firstName, String lastName, double matchPercentage) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matchPercentage = matchPercentage;
    }

    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getMatchPercentage() { return matchPercentage; }
}

