package org.example.librex.users;

import jakarta.persistence.*;
import org.example.librex.dictionaries.countries.Countries;
import org.example.librex.dictionaries.permissions.Permissions;

import java.util.Optional;

@Entity
public class Users {

    @Id
    @GeneratedValue
    private int UserID;

    @ManyToOne
    @JoinColumn(name = "PermissionID")
    private Permissions PermissionID;

    //TODO: Make nullable
    @ManyToOne
    @JoinColumn(name = "CountryID", nullable = true)
    private Countries CountryID;

    private String Firstname;
    private String Surname;
    private String Birthdate;
    private String Address;
    private String Phone;
    private String Email;
    private String Username;
    private String Password;
    private Boolean IsBlacklisted;

    public Users(Permissions Permission, Countries Country, String Firstname, String Surname, String Birthdate, String Address, String Phone, String Email, String Username, String Password, Boolean IsBlacklisted) {
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

    public Users() {
    }
}
