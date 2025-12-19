package com.servify.chat.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.repository.BookingRepository;
import com.servify.chat.dto.ChatMessageResponse;
import com.servify.chat.model.ChatMessageEntity;
import com.servify.chat.repository.ChatMessageRepository;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.enums.Role;
import com.servify.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final BookingRepository bookingRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    @Override
    public ChatMessageResponse sendMessage(Long bookingId, String content, String senderEmail) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        UserEntity sender = findUserByEmail(senderEmail);
        validateParticipant(booking, sender);

        ChatMessageEntity message = new ChatMessageEntity();
        message.setBooking(booking);
        message.setSenderId(sender.getUserId());
        message.setSenderRole(sender.getRole());
        message.setContent(content.trim());

        ChatMessageEntity saved = chatMessageRepository.save(message);
        return mapToResponse(saved);
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long bookingId, String requesterEmail) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        UserEntity requester = findUserByEmail(requesterEmail);
        validateParticipant(booking, requester);

        return chatMessageRepository.findByBookingBookingIdOrderByCreatedAtAsc(bookingId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserEntity findUserByEmail(String email) {
        return clientRepository.findByEmail(email)
                .<UserEntity>map(client -> client)
                .or(() -> providerRepository.findByEmail(email).map(provider -> provider))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private void validateParticipant(BookingEntity booking, UserEntity user) {
        boolean isClient = booking.getClient().getUserId().equals(user.getUserId()) && user.getRole() == Role.CLIENT;
        boolean isProvider = booking.getProvider().getUserId().equals(user.getUserId()) && user.getRole() == Role.PROVIDER;
        if (!isClient && !isProvider) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this booking");
        }
    }

    private ChatMessageResponse mapToResponse(ChatMessageEntity message) {
        return ChatMessageResponse.builder()
                .messageId(message.getMessageId())
                .bookingId(message.getBooking().getBookingId())
                .senderId(message.getSenderId())
                .senderRole(message.getSenderRole())
                .content(message.getContent())
                .createdAt(message.getCreatedAt().toEpochMilli())
                .build();
    }
}
