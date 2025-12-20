import { Injectable, OnDestroy } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { BehaviorSubject, catchError, filter, of, Subscription, switchMap, timer } from 'rxjs';
import { ChatConversation } from '../models/chat-message.model';
import { ChatService } from './chat.service';

type LastSeenMap = Record<number, number>;

@Injectable({ providedIn: 'root' })
export class ChatNotificationService implements OnDestroy {
  private readonly storageKey = 'chat_last_seen';
  private readonly pollingIntervalMs = 10000;
  private readonly unreadCountSubject = new BehaviorSubject<number>(0);
  private readonly routerSubscription: Subscription;
  private pollingSubscription?: Subscription;
  private latestConversations: ChatConversation[] = [];

  unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private readonly chatService: ChatService, private readonly router: Router) {
    this.routerSubscription = this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => {
        if (this.isChatsRoute(event.urlAfterRedirects)) {
          this.markAllAsRead();
        }
      });
  }

  startPolling() {
    if (this.pollingSubscription) {
      return;
    }

    this.pollingSubscription = timer(0, this.pollingIntervalMs)
      .pipe(
        switchMap(() =>
          this.chatService.getConversations().pipe(catchError(() => of([] as ChatConversation[]))),
        ),
      )
      .subscribe((conversations) => {
        this.latestConversations = conversations;
        this.updateUnreadCount(conversations);
      });
  }

  markConversationAsRead(bookingId: number, timestamp = Date.now()) {
    const lastSeen = this.getLastSeenMap();
    lastSeen[bookingId] = timestamp;
    this.saveLastSeenMap(lastSeen);
    this.updateUnreadCount(this.latestConversations);
  }

  markAllAsRead() {
    if (!this.latestConversations.length) {
      return;
    }
    const now = Date.now();
    const lastSeen = this.getLastSeenMap();
    for (const conversation of this.latestConversations) {
      lastSeen[conversation.bookingId] = now;
    }
    this.saveLastSeenMap(lastSeen);
    this.updateUnreadCount(this.latestConversations);
  }

  ngOnDestroy(): void {
    this.pollingSubscription?.unsubscribe();
    this.routerSubscription.unsubscribe();
  }

  private updateUnreadCount(conversations: ChatConversation[]) {
    const lastSeen = this.getLastSeenMap();
    const unreadCount = conversations.filter(
      (conversation) =>
        conversation.lastMessageAt > (lastSeen[conversation.bookingId] ?? 0),
    ).length;
    this.unreadCountSubject.next(unreadCount);
  }

  private getLastSeenMap(): LastSeenMap {
    const stored = localStorage.getItem(this.storageKey);
    if (!stored) {
      return {};
    }
    try {
      return JSON.parse(stored) as LastSeenMap;
    } catch {
      return {};
    }
  }

  private saveLastSeenMap(map: LastSeenMap) {
    localStorage.setItem(this.storageKey, JSON.stringify(map));
  }

  private isChatsRoute(url: string) {
    return url.startsWith('/clients/chats') || url.startsWith('/providers/chats');
  }
}
