package ru.development.UMS.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role", nullable = false, unique = true, length = 45)
    private String role;

    @Column(name = "description", length = 45)
    private String description;
}
