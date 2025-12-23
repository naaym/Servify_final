package com.servify.chat.admin.repository;

import com.servify.chat.admin.model.AdminChatThreadEntity;
import com.servify.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminChatThreadRepository extends JpaRepository<AdminChatThreadEntity, Long> {
    List<AdminChatThreadEntity> findByParticipantIdAndParticipantRole(Long participantId, Role participantRole);

    List<AdminChatThreadEntity> findByAdminId(Long adminId);

    Optional<AdminChatThreadEntity> findByParticipantIdAndParticipantRoleAndAdminId(
            Long participantId,
            Role participantRole,
            Long adminId
    );
}
