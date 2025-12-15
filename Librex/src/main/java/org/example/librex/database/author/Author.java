package org.example.librex.database.author;

import jakarta.persistence.*;
import org.example.librex.database.dictionaries.genre.Genre;

import java.time.LocalDate;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer id;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "deathdate")
    private LocalDate deathdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    protected Author() {
    }

    public Author(String firstname,
                  String surname,
                  String nickname,
                  String nationality,
                  LocalDate birthdate,
                  LocalDate deathdate,
                  Genre genre) {
        this.firstname = firstname;
        this.surname = surname;
        this.nickname = nickname;
        this.nationality = nationality;
        this.birthdate = birthdate;
        this.deathdate = deathdate;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getDeathdate() {
        return deathdate;
    }

    public void setDeathdate(LocalDate deathdate) {
        this.deathdate = deathdate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
