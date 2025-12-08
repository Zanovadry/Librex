package org.example.librex.dictionaries.languages;


import jakarta.persistence.*;

@Entity
@Table(name = "languages_dict")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, unique = true, length = 50)
    private LanguageName name;

    public Language() {}

    public Language(LanguageName name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public LanguageName getName() {
        return name;
    }
}
