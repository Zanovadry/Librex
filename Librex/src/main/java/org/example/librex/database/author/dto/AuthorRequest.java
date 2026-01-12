package org.example.librex.database.author.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AuthorRequest {

    @NotBlank
    @Size(max = 50)
    private String firstname;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @NotBlank
    @Size(max = 50)
    private String nickname;

    @Size(max = 100)
    private String nationality;

    // format: "2000-01-01"
    private LocalDate birthdate;

    private LocalDate deathdate;

    // id z tabeli genre_dict
    private Integer genreId;

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

    public Integer getGenreId() {
        return genreId;
    }
}
