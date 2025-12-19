export interface ChatMessage {
  messageId: number;
  bookingId: number;
  senderId: number;
  senderRole: string;
  content: string;
  createdAt: number;
}

export interface ChatMessageRequest {
  bookingId: number;
  content: string;
}
