package com.servify.review.repository;

import com.servify.review.dto.ReviewSummary;
import com.servify.review.model.ReviewEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    boolean existsByBooking_BookingId(Long bookingId);

    Optional<ReviewEntity> findByBooking_BookingId(Long bookingId);

    List<ReviewEntity> findByProviderUserIdOrderByCreatedAtDesc(Long providerId);

    @Query("""
        select new com.servify.review.dto.ReviewSummary(
            avg(r.politenessRating),
            avg(r.qualityRating),
            avg(r.punctualityRating),
            avg((r.politenessRating + r.qualityRating + r.punctualityRating) / 3.0),
            count(r)
        )
        from ReviewEntity r
        where r.provider.userId = :providerId
        """)
    ReviewSummary findSummaryByProviderId(@Param("providerId") Long providerId);
}
