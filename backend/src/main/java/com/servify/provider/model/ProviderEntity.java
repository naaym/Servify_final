package com.servify.provider.model;

import com.servify.user.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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


    private String cin;

    private String cv;

    private String diplome;




}
