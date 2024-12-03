package com.example.croissant;

public class UserComparison {
    private String email;
    private double percentage;

    public UserComparison(String email, double percentage) {
        this.email = email;
        this.percentage = percentage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}