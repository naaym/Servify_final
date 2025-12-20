import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ChatConversation } from '../chat/models/chat-message.model';
import { ChatNotificationService } from '../chat/services/chat-notification.service';
import { ChatService } from '../chat/services/chat.service';
import { BookingChatComponent } from '../chat/components/booking-chat/booking-chat.component';

@Component({
  selector: 'app-booking-chats',
  standalone: true,
  imports: [CommonModule, FormsModule, BookingChatComponent],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss',
})
export class BookingChatsComponent implements OnInit, OnDestroy {
  conversations: ChatConversation[] = [];
  filteredConversations: ChatConversation[] = [];
  selectedConversation: ChatConversation | null = null;
  searchTerm = '';
  loading = false;
  errorMessage = '';
  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly chatService: ChatService,
    private readonly chatNotificationService: ChatNotificationService,
  ) {}

  ngOnInit(): void {
    this.loadConversations();
    this.chatNotificationService.markAllAsRead();
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadConversations() {
    this.loading = true;
    this.chatService
      .getConversations()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (conversations) => {
          this.conversations = conversations;
          this.applyFilter();
          if (!this.selectedConversation && this.filteredConversations.length) {
    this.selectedConversation = this.filteredConversations[0];
  }

  this.loading = false;
        },
        error: (err) => {
          this.errorMessage = err.message;
          this.loading = false;
        },
      });
  }

  selectConversation(conversation: ChatConversation) {
    this.selectedConversation = conversation;
    this.chatNotificationService.markConversationAsRead(conversation.bookingId);
  }

  applyFilter() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredConversations = [...this.conversations];
      return;
    }
    this.filteredConversations = this.conversations.filter((conversation) =>
      conversation.participantName.toLowerCase().includes(term),
    );
  }

}
