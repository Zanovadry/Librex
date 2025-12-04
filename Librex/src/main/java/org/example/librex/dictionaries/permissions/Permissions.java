package org.example.librex.dictionaries.permissions;

import jakarta.persistence.*;

@Entity
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PermissionID;

    //TODO: Determine if using enum is a good idea
    @Enumerated(EnumType.STRING)
    private Role Role;
}
