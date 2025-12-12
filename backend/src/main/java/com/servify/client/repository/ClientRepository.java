package com.servify.client.repository;

import com.servify.client.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
