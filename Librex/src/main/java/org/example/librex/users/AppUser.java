package org.example.librex.users;

import jakarta.persistence.*;
import org.example.librex.dictionaries.countries.Country;
import org.example.librex.dictionaries.permissions.Permission;

@Entity
public class AppUser {

    @Id
    @GeneratedValue
    private int UserID;

    @ManyToOne
    @JoinColumn(name = "PermissionID", nullable = false)
    private Permission PermissionID;

    //Nullable
    @ManyToOne
    @JoinColumn(name = "CountryID")
    private Country CountryID;

    private String Firstname;
    private String Surname;
    private String Birthdate;
    private String Address;
    private String Phone;
    private String Email;
    private String Username;
    private String Password;
    private Boolean IsBlacklisted;

    public AppUser(Permission Permission, Country Country, String Firstname, String Surname, String Birthdate, String Address, String Phone, String Email, String Username, String Password, Boolean IsBlacklisted) {
        this.PermissionID = Permission;
        this.CountryID = Country;
        this.Firstname = Firstname;
        this.Surname = Surname;
        this.Birthdate = Birthdate;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
        this.Username = Username;
        this.Password = Password;
        this.IsBlacklisted = IsBlacklisted;

    }

    public AppUser() {
    }

    public Permission getPermissionID() {
        return PermissionID;
    }

    public Country getCountryID() {
        return CountryID;
    }

    public String getFirstname() {
        return Firstname;
    }

    public String getSurname() {
        return Surname;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public Boolean getBlacklisted() {
        return IsBlacklisted;
    }

    public int getUserID() {
        return UserID;
    }
}
