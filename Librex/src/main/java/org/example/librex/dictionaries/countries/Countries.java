package org.example.librex.dictionaries.countries;

import jakarta.persistence.*;

@Entity
public class Countries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CountryID;

    //TODO: Determine if using enum is a good idea
    @Enumerated(EnumType.STRING)
    private Country CountryName;

    public int getCountryID() {
        return CountryID;
    }

    public Country getCountryName() {
        return CountryName;
    }
}
