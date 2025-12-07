package org.example.librex.dictionaries.permissions;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions_dict")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = true, length = 50)
    private Role role;

    protected Permission() {
    }

    public Permission(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
