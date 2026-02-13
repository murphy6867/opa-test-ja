package com.opa.transferservice.dto;

public class User {
    private String username;
    private String role;
    private double balance;

    public User(String username, String role, double balance) {
        this.username = username;
        this.role = role;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
