package org.example.librex.database.publisher;

import jakarta.persistence.*;
import org.example.librex.database.dictionaries.country.Country;

import java.time.LocalDate;

@Entity
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Integer publisherId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;   // można później zmienić na @ManyToOne Country

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "webpage", length = 100)
    private String webpage;

    @Column(name = "foundation_date")
    private LocalDate foundationDate;

    protected Publisher() {
    }

    public Publisher(Integer publisherId,
                     String name,
                     String address,
                     Country country,
                     String email,
                     String webpage,
                     LocalDate foundationDate) {
        this.publisherId = publisherId;
        this.name = name;
        this.address = address;
        this.country = country;
        this.email = email;
        this.webpage = webpage;
        this.foundationDate = foundationDate;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Country getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getWebpage() {
        return webpage;
    }

    public LocalDate getFoundationDate() {
        return foundationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public void setFoundationDate(LocalDate foundationDate) {
        this.foundationDate = foundationDate;
    }
}

