package com.servify.provider.repository;

import com.servify.provider.model.ServiceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryEntity, Long> {
    Optional<ServiceCategoryEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<ServiceCategoryEntity> findAllByOrderByNameAsc();
}
