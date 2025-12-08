package org.example.librex.database.dictionaries.genre;

import jakarta.persistence.*;

@Entity
//TODO: w schemacie jest GenreDict (raz mamy l.mn. a raz l.poj.)
@Table(name = "genres_dict")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    //TODO: name czy genre_name?
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private GenreName name;

    protected Genre() {}

    public Genre(GenreName name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public GenreName getName() {
        return name;
    }
}
