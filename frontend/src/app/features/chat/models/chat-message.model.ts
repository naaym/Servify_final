export interface ChatMessage {
  messageId: number;
  bookingId: number;
  senderId: number;
  senderRole: string;
  senderName: string;
  senderImageUrl: string | null;
  content: string;
  createdAt: number;
}

export interface ChatMessageRequest {
  bookingId: number;
  content: string;
}

export interface ChatConversation {
  bookingId: number;
  participantName: string;
  participantImageUrl: string | null;
  lastMessage: string;
  lastMessageAt: number;
}
