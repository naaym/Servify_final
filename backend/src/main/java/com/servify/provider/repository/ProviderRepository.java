package com.servify.provider.repository;

import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Optional<ProviderEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    List<ProviderEntity> findAllByStatus(ProviderStatus status);

    long countByStatus(ProviderStatus status);

    @Query("""
            SELECT p FROM ProviderEntity p
            WHERE ( LOWER(p.serviceCategory) LIKE LOWER(CONCAT('%', :serviceCategory, '%')))
            AND ( trim(LOWER(p.governorate)) = trim(LOWER(:governorate)))
            AND (:minPrice IS NULL OR p.basePrice >= :minPrice)
            AND (:maxPrice IS NULL OR p.basePrice <= :maxPrice)
            AND (:minRating IS NULL OR p.rating >= :minRating)
            AND p.status = :status
            """)
    List<ProviderEntity> searchApprovedProviders(
      @Param("serviceCategory") String serviceCategory,
      @Param("governorate") String governorate,
      @Param("minPrice") Double minPrice,
      @Param("maxPrice") Double maxPrice,
      @Param("minRating") Double minRating,
      @Param("status") ProviderStatus status);

    @Query("""
            SELECT DISTINCT LOWER(TRIM(p.serviceCategory))
            FROM ProviderEntity p
            WHERE p.status = :status
            """)
    List<String> findDistinctServiceCategoriesByStatus(@Param("status") ProviderStatus status);

    @Query("""
            SELECT DISTINCT LOWER(TRIM(p.governorate))
            FROM ProviderEntity p
            WHERE p.status = :status
            AND (:serviceCategory IS NULL OR LOWER(TRIM(p.serviceCategory)) = LOWER(TRIM(:serviceCategory)))
            """)
    List<String> findDistinctGovernoratesByStatusAndServiceCategory(
            @Param("status") ProviderStatus status,
            @Param("serviceCategory") String serviceCategory);
}
