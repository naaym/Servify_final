package com.servify.booking.service;

import com.servify.booking.dto.BookingDetailsResponse;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.dto.BookingStatsResponse;
import com.servify.booking.dto.FileMetadata;
import com.servify.booking.dto.ProviderDetails;
import com.servify.booking.model.BookingAttachmentEntity;
import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.repository.BookingRepository;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.review.dto.ReviewRequest;
import com.servify.review.dto.ReviewResponse;
import com.servify.review.dto.ReviewSummary;
import com.servify.review.model.ReviewEntity;
import com.servify.review.repository.ReviewRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ProviderRepository providerRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public BookingResponse createBooking(Long providerId, String date, String time, String description, List<MultipartFile> attachments) {
        ClientEntity client = getCurrentClient();
        ProviderEntity provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        BookingEntity booking = new BookingEntity();
        booking.setClient(client);
        booking.setProvider(provider);
        booking.setAppointmentDate(LocalDate.parse(date));
        booking.setAppointmentTime(LocalTime.parse(time));
        booking.setDescription(description);
        booking.setStatus(BookingStatus.PENDING);

        if (attachments != null) {
            attachments.stream()
                    .filter(Objects::nonNull)
                    .filter(file -> !file.isEmpty())
                    .forEach(file -> booking.addAttachment(mapToAttachment(file)));
        }

        BookingEntity saved = bookingRepository.save(booking);
        return mapToBookingResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {
        ClientEntity client = getCurrentClient();
        return bookingRepository.findByClientUserIdOrderByCreatedAtDesc(client.getUserId())
                .stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingStatsResponse getMyStats() {
        ClientEntity client = getCurrentClient();
        Long clientId = client.getUserId();
        return BookingStatsResponse.builder()
                .totalRequests(bookingRepository.countByClientUserId(clientId))
                .totalPending(bookingRepository.countByClientUserIdAndStatus(clientId, BookingStatus.PENDING))
                .totalAccepted(bookingRepository.countByClientUserIdAndStatus(clientId, BookingStatus.ACCEPTED))
                .totalRejected(bookingRepository.countByClientUserIdAndStatus(clientId, BookingStatus.REJECTED))
                .totalCancelled(bookingRepository.countByClientUserIdAndStatus(clientId, BookingStatus.CANCELLED))
                .totalDone(bookingRepository.countByClientUserIdAndStatus(clientId, BookingStatus.DONE))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailsResponse getBookingDetails(Long bookingId) {
        BookingEntity booking = findOwnedBooking(bookingId);
        return mapToBookingDetails(booking);
    }

    @Override
    public BookingDetailsResponse cancelBooking(Long bookingId) {
        BookingEntity booking = findOwnedBooking(bookingId);

        if (booking.getStatus() == BookingStatus.DONE || booking.getStatus() == BookingStatus.CANCELLED) {
            return mapToBookingDetails(booking);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        BookingEntity saved = bookingRepository.save(booking);
        return mapToBookingDetails(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getProviderBookings() {
        ProviderEntity provider = getCurrentProvider();
        return bookingRepository.findByProviderUserIdOrderByCreatedAtDesc(provider.getUserId())
                .stream()
                .map(this::mapToProviderBookingResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailsResponse getProviderBookingDetails(Long bookingId) {
        BookingEntity booking = findOwnedBookingForProvider(bookingId);
        return mapToBookingDetails(booking);
    }

    @Override
    public BookingDetailsResponse updateStatusAsProvider(Long bookingId, BookingStatus status) {
        BookingEntity booking = findOwnedBookingForProvider(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.DONE) {
            return mapToBookingDetails(booking);
        }

        boolean isAllowedTransition =
                status == BookingStatus.ACCEPTED ||
                status == BookingStatus.REJECTED ||
                (status == BookingStatus.DONE && booking.getStatus() == BookingStatus.ACCEPTED);

        if (!isAllowedTransition) {
            return mapToBookingDetails(booking);
        }

        booking.setStatus(status);
        BookingEntity saved = bookingRepository.save(booking);
        return mapToBookingDetails(saved);
    }

    @Override
    public ReviewResponse submitReview(Long bookingId, ReviewRequest request) {
        BookingEntity booking = findOwnedBooking(bookingId);

        if (booking.getStatus() != BookingStatus.DONE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Booking must be done to review");
        }

        if (reviewRepository.existsByBooking_BookingId(bookingId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already submitted");
        }

        ReviewEntity review = new ReviewEntity();
        review.setBooking(booking);
        review.setProvider(booking.getProvider());
        review.setClient(booking.getClient());
        review.setPolitenessRating(request.getPolitenessRating());
        review.setQualityRating(request.getQualityRating());
        review.setPunctualityRating(request.getPunctualityRating());
        review.setComment(normalizeComment(request.getComment()));

        ReviewEntity savedReview = reviewRepository.save(review);
        updateProviderRating(booking.getProvider());

        return ReviewResponse.builder()
                .id(savedReview.getId())
                .bookingId(bookingId)
                .overallRating(calculateOverallRating(savedReview))
                .politenessRating(savedReview.getPolitenessRating())
                .qualityRating(savedReview.getQualityRating())
                .punctualityRating(savedReview.getPunctualityRating())
                .comment(savedReview.getComment())
                .createdAt(savedReview.getCreatedAt().toEpochMilli())
                .build();
    }

    private BookingEntity findOwnedBooking(Long bookingId) {
        ClientEntity client = getCurrentClient();
        return bookingRepository.findByBookingIdAndClientUserId(bookingId, client.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    private BookingEntity findOwnedBookingForProvider(Long bookingId) {
        ProviderEntity provider = getCurrentProvider();
        return bookingRepository.findByBookingIdAndProviderUserId(bookingId, provider.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    private ClientEntity getCurrentClient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    private ProviderEntity getCurrentProvider() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return providerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
    }

    private BookingAttachmentEntity mapToAttachment(MultipartFile file) {
        try {
            BookingAttachmentEntity attachment = new BookingAttachmentEntity();
            attachment.setContentType(file.getContentType());
            attachment.setFileName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            return attachment;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store attachment", e);
        }
    }

    private BookingResponse mapToBookingResponse(BookingEntity booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .status(booking.getStatus())
                .category(booking.getProvider().getServiceCategory())
                .providerName(booking.getProvider().getName())
                .clientName(booking.getClient().getName())
                .date(toEpochMilli(booking))
                .build();
    }

    private BookingResponse mapToProviderBookingResponse(BookingEntity booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .status(booking.getStatus())
                .category(booking.getProvider().getServiceCategory())
                .clientName(booking.getClient().getName())
                .date(toEpochMilli(booking))
                .build();
    }

    private BookingDetailsResponse mapToBookingDetails(BookingEntity booking) {
        return BookingDetailsResponse.builder()
                .bookingId(booking.getBookingId())
                .status(booking.getStatus())
                .bookingStatus(booking.getStatus())
                .description(booking.getDescription())
                .date(toEpochMilli(booking))
                .updatedAt(booking.getUpdatedAt().toEpochMilli())
                .serviceGategory(booking.getProvider().getServiceCategory())
                .providerInfo(mapToProviderDetails(booking.getProvider()))
                .clientName(booking.getClient().getName())
                .clientPhone(booking.getClient().getPhone())
                .attachments(mapToFileMetadata(booking.getAttachments()))
                .reviewSubmitted(reviewRepository.existsByBooking_BookingId(booking.getBookingId()))
                .build();
    }

    private ProviderDetails mapToProviderDetails(ProviderEntity provider) {
        return ProviderDetails.builder()
                .providerId(provider.getUserId())
                .providerLocalisation(provider.getGovernorate() + " - " + provider.getDelegation())
                .providerName(provider.getName())
                .providerPhone(provider.getPhone())
                .providerRate(provider.getRating() != null ? provider.getRating() : 0.0)
                .profileImageUrl(provider.getProfileImageUrl())
                .build();
    }

    private List<FileMetadata> mapToFileMetadata(List<BookingAttachmentEntity> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return Collections.emptyList();
        }

        return attachments.stream()
                .map(att -> FileMetadata.builder()
                        .name(att.getFileName())
                        .url(buildDataUrl(att))
                        .build())
                .toList();
    }

    private String buildDataUrl(BookingAttachmentEntity attachment) {
        String base64 = Base64.getEncoder().encodeToString(attachment.getData());
        return "data:" + attachment.getContentType() + ";base64," + base64;
    }

    private long toEpochMilli(BookingEntity booking) {
        LocalDateTime dateTime = LocalDateTime.of(booking.getAppointmentDate(), booking.getAppointmentTime());
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private void updateProviderRating(ProviderEntity provider) {
        ReviewSummary summary = reviewRepository.findSummaryByProviderId(provider.getUserId());
        if (summary == null || summary.reviewCount() == null || summary.reviewCount() == 0) {
            provider.setRating(0.0);
            provider.setReviewCount(0);
            providerRepository.save(provider);
            return;
        }
        provider.setRating(roundRating(summary.overallAverage()));
        provider.setReviewCount(summary.reviewCount().intValue());
        providerRepository.save(provider);
    }

    private Double calculateOverallRating(ReviewEntity review) {
        return roundRating((review.getPolitenessRating() + review.getQualityRating() + review.getPunctualityRating()) / 3.0);
    }

    private Double roundRating(Double rating) {
        if (rating == null) {
            return 0.0;
        }
        return Math.round(rating * 100.0) / 100.0;
    }

    private String normalizeComment(String comment) {
        if (comment == null) {
            return null;
        }
        String trimmed = comment.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
