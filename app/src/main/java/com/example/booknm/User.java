package com.example.booknm;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;

    // Constructor with all fields
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor without id (useful for creating new users before they are saved to a database)
    public User(String name, String email, String password, String role) {
        this(-1, name, email, password, role); // Calls the main constructor with a default id
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // --- Setters (example for role) ---
    public void setRole(String role) {
        this.role = role;
    }
}
