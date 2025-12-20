package com.servify.chat.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.repository.BookingRepository;
import com.servify.chat.dto.ChatConversationResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
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

    @Override
    public List<ChatConversationResponse> getConversations(String requesterEmail) {
        UserEntity requester = findUserByEmail(requesterEmail);
        List<ChatMessageEntity> messages;
        if (requester.getRole() == Role.CLIENT) {
            messages = chatMessageRepository.findByBookingClientUserIdOrderByCreatedAtDesc(requester.getUserId());
        } else if (requester.getRole() == Role.PROVIDER) {
            messages = chatMessageRepository.findByBookingProviderUserIdOrderByCreatedAtDesc(requester.getUserId());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for messages");
        }

        Map<Long, ChatConversationResponse> conversations = new LinkedHashMap<>();
        for (ChatMessageEntity message : messages) {
            Long bookingId = message.getBooking().getBookingId();
            if (conversations.containsKey(bookingId)) {
                continue;
            }
            BookingEntity booking = message.getBooking();
            String participantName;
            String participantImageUrl;
            if (requester.getRole() == Role.CLIENT) {
                participantName = booking.getProvider().getName();
                participantImageUrl = booking.getProvider().getProfileImageUrl();
            } else {
                participantName = booking.getClient().getName();
                participantImageUrl = booking.getClient().getProfileImageUrl();
            }
            conversations.put(bookingId, ChatConversationResponse.builder()
                    .bookingId(bookingId)
                    .participantName(participantName)
                    .participantImageUrl(participantImageUrl)
                    .lastMessage(message.getContent())
                    .lastMessageAt(message.getCreatedAt().toEpochMilli())
                    .build());
        }

        return new ArrayList<>(conversations.values());
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
        BookingEntity booking = message.getBooking();
        String senderName;
        String senderImageUrl;
        if (message.getSenderRole() == Role.CLIENT) {
            senderName = booking.getClient().getName();
            senderImageUrl = booking.getClient().getProfileImageUrl();
        } else {
            senderName = booking.getProvider().getName();
            senderImageUrl = booking.getProvider().getProfileImageUrl();
        }

        return ChatMessageResponse.builder()
                .messageId(message.getMessageId())
                .bookingId(message.getBooking().getBookingId())
                .senderId(message.getSenderId())
                .senderRole(message.getSenderRole())
                .senderName(senderName)
                .senderImageUrl(senderImageUrl)
                .content(message.getContent())
                .createdAt(message.getCreatedAt().toEpochMilli())
                .build();
    }
}
