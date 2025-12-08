package org.example.librex.database.dictionaries.country;

import jakarta.persistence.*;

@Entity
@Table(name = "countries_dict")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_name", nullable = false, unique = true, length = 100)
    private CountryName name;

    protected Country() {
    }

    public Country(CountryName name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public CountryName getName() {
        return name;
    }
}
