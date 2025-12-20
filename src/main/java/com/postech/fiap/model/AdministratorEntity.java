package com.postech.fiap.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "administrator", schema = "public")
public class AdministratorEntity extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "administratorSequence",
            sequenceName = "administrator_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "administratorSequence")
    public Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    public AdministratorEntity() {
    }

    public AdministratorEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

