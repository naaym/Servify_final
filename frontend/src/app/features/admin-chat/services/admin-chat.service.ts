import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { Http } from '../../../core/api/http';
import { TokenService } from '../../../core/services/token.service';
import {
  AdminChatAdmin,
  AdminChatConversation,
  AdminChatMessage,
  AdminChatMessageRequest,
} from '../models/admin-chat.model';

@Injectable({ providedIn: 'root' })
export class AdminChatService {
  private client: Client | null = null;
  private messageSubject = new Subject<AdminChatMessage>();
  private currentThreadId: number | null = null;

  constructor(
    private readonly http: Http,
    private readonly tokenService: TokenService,
  ) {}

  connect(threadId: number): Observable<AdminChatMessage> {
    if (this.client?.active && this.currentThreadId === threadId) {
      return this.messageSubject.asObservable();
    }

    this.disconnect();
    this.currentThreadId = threadId;

    const token = this.tokenService.getAccessToken();

    this.client = new Client({
      webSocketFactory: () => new SockJS(API_ENDPOINTS.WS_BASE),
      reconnectDelay: 5000,
      connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    });

    this.client.onConnect = () => {
      this.client?.subscribe(`/topic/admin-chats/${threadId}`, (message: IMessage) => {
        const payload = JSON.parse(message.body) as AdminChatMessage;
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

  sendMessage(request: AdminChatMessageRequest) {
    if (!this.client) {
      return;
    }
    this.client.publish({
      destination: '/app/admin-chat.send',
      body: JSON.stringify(request),
    });
  }

  getConversations() {
    return this.http.get<AdminChatConversation[]>(API_ENDPOINTS.ADMIN_CHAT.THREADS);
  }

  getMessages(threadId: number) {
    return this.http.get<AdminChatMessage[]>(API_ENDPOINTS.ADMIN_CHAT.THREAD_MESSAGES(threadId));
  }

  getAdmins() {
    return this.http.get<AdminChatAdmin[]>(API_ENDPOINTS.ADMIN_CHAT.ADMINS);
  }

  createThread(adminId: number) {
    return this.http.post<AdminChatConversation>(API_ENDPOINTS.ADMIN_CHAT.THREADS, { adminId });
  }
}
