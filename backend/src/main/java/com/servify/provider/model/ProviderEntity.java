package com.servify.provider.model;

import com.servify.user.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "providers")
public class ProviderEntity extends UserEntity {

    @Column(nullable = false)
    private String delegation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderStatus status;
    private int age;

    @Column(nullable = false)
    private String serviceCategory;

    private Double basePrice;

    private Double rating;

    private Integer reviewCount;

    private String description;


    private String cinUrl;

    private String cvUrl;

    private String diplomeUrl;
    private String cinName;
    private String cvName;
    private String diplomeName;






    @PrePersist
    @PreUpdate
    void normalizeProviderFields() {
        if (delegation != null) {
            delegation = delegation.trim();
        }
        if (serviceCategory != null) {
            serviceCategory = serviceCategory.trim().toLowerCase();
        }
    }
}
