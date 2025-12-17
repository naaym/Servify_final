package com.servify.provider.repository;

import com.servify.provider.model.GovernorateEntity;
import com.servify.provider.model.ServiceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovernorateRepository extends JpaRepository<GovernorateEntity, Long> {
    boolean existsByServiceAndNameIgnoreCase(ServiceCategoryEntity service, String name);

    List<GovernorateEntity> findByServiceOrderByNameAsc(ServiceCategoryEntity service);
}
