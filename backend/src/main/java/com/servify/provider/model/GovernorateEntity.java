package com.servify.provider.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "governorates", uniqueConstraints = @UniqueConstraint(columnNames = {"service_id", "name"}))
public class GovernorateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceCategoryEntity service;

    @Column(nullable = false)
    private String name;

    @PrePersist
    @PreUpdate
    void normalizeName() {
        if (name != null) {
            name = name.trim().toLowerCase();
        }
    }
}
