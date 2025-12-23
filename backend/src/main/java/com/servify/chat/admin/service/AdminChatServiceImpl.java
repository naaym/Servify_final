package com.servify.chat.admin.service;

import com.servify.admin.repository.AdminRepository;
import com.servify.chat.admin.dto.AdminChatAdminResponse;
import com.servify.chat.admin.dto.AdminChatConversationResponse;
import com.servify.chat.admin.dto.AdminChatMessageResponse;
import com.servify.chat.admin.model.AdminChatMessageEntity;
import com.servify.chat.admin.model.AdminChatThreadEntity;
import com.servify.chat.admin.repository.AdminChatMessageRepository;
import com.servify.chat.admin.repository.AdminChatThreadRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.enums.Role;
import com.servify.user.model.UserEntity;
import com.servify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminChatServiceImpl implements AdminChatService {

    private final AdminChatThreadRepository threadRepository;
    private final AdminChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminChatConversationResponse> getConversations(String requesterEmail) {
        UserEntity requester = findUserByEmail(requesterEmail);
        List<AdminChatThreadEntity> threads;
        if (requester.getRole() == Role.ADMIN) {
            threads = threadRepository.findByAdminId(requester.getUserId());
        } else if (requester.getRole() == Role.CLIENT || requester.getRole() == Role.PROVIDER
                || requester.getRole() == Role.SUPER_ADMIN) {
            threads = threadRepository.findByParticipantIdAndParticipantRole(
                    requester.getUserId(),
                    requester.getRole()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for admin chat");
        }

        return threads.stream()
                .map(thread -> buildConversation(thread, requester))
                .sorted(Comparator.comparing(AdminChatConversationResponse::getLastMessageAt).reversed())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminChatMessageResponse> getMessages(Long threadId, String requesterEmail) {
        UserEntity requester = findUserByEmail(requesterEmail);
        AdminChatThreadEntity thread = getThread(threadId);
        validateAccess(thread, requester);
        return messageRepository.findByThreadThreadIdOrderByCreatedAtAsc(threadId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminChatMessageResponse sendMessage(Long threadId, String content, String senderEmail) {
        UserEntity sender = findUserByEmail(senderEmail);
        AdminChatThreadEntity thread = getThread(threadId);
        validateAccess(thread, sender);

        String trimmed = content == null ? "" : content.trim();
        if (trimmed.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content is required");
        }

        AdminChatMessageEntity message = new AdminChatMessageEntity();
        message.setThread(thread);
        message.setSenderId(sender.getUserId());
        message.setSenderRole(sender.getRole());
        message.setContent(trimmed);

        return mapToResponse(messageRepository.save(message));
    }

    @Override
    public AdminChatConversationResponse createThread(Long adminId, String requesterEmail) {
        UserEntity requester = findUserByEmail(requesterEmail);
        if (requester.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admins cannot start new threads here");
        }
        if (requester.getRole() == Role.SUPER_ADMIN) {
            validateAdminTarget(adminId);
        } else if (requester.getRole() == Role.CLIENT || requester.getRole() == Role.PROVIDER) {
            validateAdminTarget(adminId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for admin chat");
        }

        AdminChatThreadEntity thread = threadRepository
                .findByParticipantIdAndParticipantRoleAndAdminId(
                        requester.getUserId(),
                        requester.getRole(),
                        adminId
                )
                .orElseGet(() -> {
                    AdminChatThreadEntity created = new AdminChatThreadEntity();
                    created.setAdminId(adminId);
                    created.setParticipantId(requester.getUserId());
                    created.setParticipantRole(requester.getRole());
                    return threadRepository.save(created);
                });

        return buildConversation(thread, requester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminChatAdminResponse> getAvailableAdmins() {
        return adminRepository.findAll().stream()
                .filter(admin -> admin.getRole() == Role.ADMIN)
                .map(admin -> AdminChatAdminResponse.builder()
                        .adminId(admin.getUserId())
                        .name(admin.getName())
                        .email(admin.getEmail())
                        .profileImageUrl(admin.getProfileImageUrl())
                        .build())
                .toList();
    }

    private AdminChatThreadEntity getThread(Long threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat thread not found"));
    }

    private AdminChatConversationResponse buildConversation(AdminChatThreadEntity thread, UserEntity requester) {
        AdminChatMessageEntity lastMessage = messageRepository
                .findTopByThreadThreadIdOrderByCreatedAtDesc(thread.getThreadId())
                .orElse(null);

        UserEntity participant;
        if (requester.getRole() == Role.ADMIN) {
            participant = getUserById(thread.getParticipantId());
        } else {
            participant = getUserById(thread.getAdminId());
        }

        long lastMessageAt = lastMessage != null
                ? lastMessage.getCreatedAt().toEpochMilli()
                : thread.getCreatedAt().toEpochMilli();

        return AdminChatConversationResponse.builder()
                .threadId(thread.getThreadId())
                .participantName(participant.getName())
                .participantImageUrl(participant.getProfileImageUrl())
                .participantRole(participant.getRole())
                .lastMessage(lastMessage != null ? lastMessage.getContent() : "")
                .lastMessageAt(lastMessageAt)
                .build();
    }

    private AdminChatMessageResponse mapToResponse(AdminChatMessageEntity message) {
        UserEntity sender = getUserById(message.getSenderId());
        return AdminChatMessageResponse.builder()
                .messageId(message.getMessageId())
                .threadId(message.getThread().getThreadId())
                .senderId(message.getSenderId())
                .senderRole(message.getSenderRole())
                .senderName(sender.getName())
                .senderImageUrl(sender.getProfileImageUrl())
                .content(message.getContent())
                .createdAt(message.getCreatedAt().toEpochMilli())
                .build();
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateAccess(AdminChatThreadEntity thread, UserEntity requester) {
        if (requester.getRole() == Role.ADMIN) {
            if (!thread.getAdminId().equals(requester.getUserId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this chat");
            }
            return;
        }

        if (requester.getRole() == Role.SUPER_ADMIN
                && thread.getParticipantRole() != Role.SUPER_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Super admin can only chat with admins");
        }

        if (!thread.getParticipantId().equals(requester.getUserId())
                || thread.getParticipantRole() != requester.getRole()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this chat");
        }
    }

    private void validateAdminTarget(Long adminId) {
        UserEntity admin = getUserById(adminId);
        if (admin.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target must be an admin");
        }
    }
}
