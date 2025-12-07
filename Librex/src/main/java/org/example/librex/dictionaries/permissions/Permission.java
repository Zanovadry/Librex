package org.example.librex.dictionaries.permissions;

import jakarta.persistence.*;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PermissionID;

    @Enumerated(EnumType.STRING)
    private Role Role;

    public int getPermissionID() {
        return PermissionID;
    }

    public Role getRole() {
        return Role;
    }
}
