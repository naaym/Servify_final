package com.servify.chat.admin.service;

import com.servify.chat.admin.dto.AdminChatAdminResponse;
import com.servify.chat.admin.dto.AdminChatConversationResponse;
import com.servify.chat.admin.dto.AdminChatMessageResponse;

import java.util.List;

public interface AdminChatService {
    List<AdminChatConversationResponse> getConversations(String requesterEmail);

    List<AdminChatMessageResponse> getMessages(Long threadId, String requesterEmail);

    AdminChatMessageResponse sendMessage(Long threadId, String content, String senderEmail);

    AdminChatConversationResponse createThread(Long adminId, String requesterEmail);

    List<AdminChatAdminResponse> getAvailableAdmins();
}
