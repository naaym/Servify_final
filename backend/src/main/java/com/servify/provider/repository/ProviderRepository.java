package com.servify.provider.repository;

import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Optional<ProviderEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    List<ProviderEntity> findAllByStatus(ProviderStatus status);
}
