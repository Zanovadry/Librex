package org.example.librex.database.users.dto;

public class UserResponse {

    private Integer id;
    private String firstname;
    private String surname;
    private String email;
    private String username;
    private String role;

    public UserResponse() {}

    public UserResponse(Integer id,
                        String firstname,
                        String surname,
                        String email,
                        String username,
                        String role) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public void setId(Integer id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }

    public Integer getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}

