package com.servify.provider.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class ServiceCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GovernorateEntity> governorates = new ArrayList<>();

    @PrePersist
    @PreUpdate
    void normalizeName() {
        if (name != null) {
            name = name.trim().toLowerCase();
        }
    }
}
