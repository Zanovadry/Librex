package org.example.librex.database.author.dto;

import java.time.LocalDate;

public class AuthorResponse {

    private Integer id;
    private String firstname;
    private String surname;
    private String nickname;
    private String nationality;
    private LocalDate birthdate;
    private LocalDate deathdate;
    private String genreName;

    public AuthorResponse(Integer id,
                          String firstname,
                          String surname,
                          String nickname,
                          String nationality,
                          LocalDate birthdate,
                          LocalDate deathdate,
                          String genreName) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.nickname = nickname;
        this.nationality = nationality;
        this.birthdate = birthdate;
        this.deathdate = deathdate;
        this.genreName = genreName;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public LocalDate getDeathdate() {
        return deathdate;
    }

    public String getGenreName() {
        return genreName;
    }
}
