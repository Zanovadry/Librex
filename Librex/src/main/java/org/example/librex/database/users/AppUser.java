package org.example.librex.database.users;

import jakarta.persistence.*;
import org.example.librex.database.dictionaries.country.Country;
import org.example.librex.database.dictionaries.permission.Permission;

import java.time.LocalDate;

@Entity
@Table(name = "user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "is_blacklisted", nullable = false)
    private boolean blacklisted = false;

    protected AppUser() {
    }

    public AppUser(Permission permission,
                   Country country,
                   String firstname,
                   String surname,
                   LocalDate birthdate,
                   String address,
                   String phone,
                   String email,
                   String username,
                   String passwordHash,
                   boolean blacklisted) {
        this.permission = permission;
        this.country = country;
        this.firstname = firstname;
        this.surname = surname;
        this.birthdate = birthdate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.blacklisted = blacklisted;
    }

    public Integer getId() {
        return id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }
}
