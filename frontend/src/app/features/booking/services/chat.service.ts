import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject, Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { Http } from '../../../core/api/http';
import { TokenService } from '../../../core/services/token.service';
import { ChatConversation, ChatMessage, ChatMessageRequest } from '../models/chat-message.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private client: Client | null = null;
  private messageSubject = new Subject<ChatMessage>();
  private currentBookingId: number | null = null;

  constructor(private readonly http: Http, private readonly tokenService: TokenService) {}

  connect(bookingId: number): Observable<ChatMessage> {
    if (this.client?.active && this.currentBookingId === bookingId) {
      return this.messageSubject.asObservable();
    }

    this.disconnect();
    this.currentBookingId = bookingId;

    const token = this.tokenService.getAccessToken();

    this.client = new Client({
      webSocketFactory: () => new SockJS(API_ENDPOINTS.WS_BASE),
      reconnectDelay: 5000,
      connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    });

    this.client.onConnect = () => {
      this.client?.subscribe(`/topic/bookings/${bookingId}`, (message: IMessage) => {
        const payload = JSON.parse(message.body) as ChatMessage;
        this.messageSubject.next(payload);
      });
    };

    this.client.activate();
    return this.messageSubject.asObservable();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
      this.client = null;
    }
  }

  sendMessage(request: ChatMessageRequest) {
    if (!this.client) {
      return;
    }
    this.client.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(request),
    });
  }

  getHistory(bookingId: number) {
    return this.http.get<ChatMessage[]>(API_ENDPOINTS.BOOKING.MESSAGES(bookingId));
  }

  getConversations() {
    return this.http.get<ChatConversation[]>(API_ENDPOINTS.MESSAGES.CONVERSATIONS);
  }
}
