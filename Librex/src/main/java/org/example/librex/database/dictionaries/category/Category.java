package org.example.librex.database.dictionaries.category;

import jakarta.persistence.*;

@Entity
@Table(name = "category_dict")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    //TODO: name czy category_name?
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private CategoryName name;

    protected Category() {}

    public Category(CategoryName name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public CategoryName getName() {
        return name;
    }
}
