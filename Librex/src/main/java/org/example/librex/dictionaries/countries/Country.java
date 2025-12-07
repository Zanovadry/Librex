package org.example.librex.dictionaries.countries;

import jakarta.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CountryID;

    @Enumerated(EnumType.STRING)
    private CountryName CountryName;

    public int getCountryID() {
        return CountryID;
    }

    public CountryName getCountryName() {
        return CountryName;
    }
}
