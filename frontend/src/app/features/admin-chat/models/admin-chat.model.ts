export interface AdminChatConversation {
  threadId: number;
  participantName: string;
  participantImageUrl: string | null;
  lastMessage: string;
  lastMessageAt: number;
  participantRole: string;
}

export interface AdminChatMessage {
  messageId: number;
  threadId: number;
  senderId: number;
  senderRole: string;
  senderName: string;
  senderImageUrl: string | null;
  content: string;
  createdAt: number;
}

export interface AdminChatMessageRequest {
  threadId: number;
  content: string;
}

export interface AdminChatAdmin {
  adminId: number;
  name: string;
  email: string;
  profileImageUrl: string | null;
}
