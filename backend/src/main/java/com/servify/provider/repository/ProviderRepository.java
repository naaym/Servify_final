package com.servify.provider.repository;

import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Optional<ProviderEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    List<ProviderEntity> findAllByStatus(ProviderStatus status);

    long countByStatus(ProviderStatus status);

    @Query("""
            SELECT p FROM ProviderEntity p
            WHERE (:serviceCategory IS NULL OR LOWER(p.serviceCategory) LIKE LOWER(CONCAT('%', :serviceCategory, '%')))
            AND (:governorate IS NULL OR LOWER(p.governorate) = LOWER(:governorate))
            AND (:delegation IS NULL OR LOWER(p.delegation) = LOWER(:delegation))
            AND (:minPrice IS NULL OR p.basePrice >= :minPrice)
            AND (:maxPrice IS NULL OR p.basePrice <= :maxPrice)
            AND (:minRating IS NULL OR p.rating >= :minRating)
            AND p.status = :status
            """)
    List<ProviderEntity> searchApprovedProviders(String serviceCategory,
                                                 String governorate,
                                                 String delegation,
                                                 Double minPrice,
                                                 Double maxPrice,
                                                 Double minRating,
                                                 ProviderStatus status);
}
