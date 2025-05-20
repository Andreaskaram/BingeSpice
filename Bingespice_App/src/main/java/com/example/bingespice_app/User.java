package com.example.bingespice_app;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String country;

    public User(String firstName, String lastName, String email, String gender, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.country = country;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getCountry() { return country; }
}