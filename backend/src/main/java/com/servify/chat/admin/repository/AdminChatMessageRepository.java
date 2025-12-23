package com.servify.chat.admin.repository;

import com.servify.chat.admin.model.AdminChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminChatMessageRepository extends JpaRepository<AdminChatMessageEntity, Long> {
    List<AdminChatMessageEntity> findByThreadThreadIdOrderByCreatedAtAsc(Long threadId);

    Optional<AdminChatMessageEntity> findTopByThreadThreadIdOrderByCreatedAtDesc(Long threadId);
}
