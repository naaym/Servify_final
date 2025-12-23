import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { TokenService } from '../../core/services/token.service';
import {
  AdminChatAdmin,
  AdminChatConversation,
} from './models/admin-chat.model';
import { AdminChatService } from './services/admin-chat.service';
import { AdminChatThreadComponent } from './components/admin-chat-thread/admin-chat-thread.component';

@Component({
  selector: 'app-admin-chats',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminChatThreadComponent],
  templateUrl: './admin-chat.component.html',
  styleUrl: './admin-chat.component.scss',
})
export class AdminChatsComponent implements OnInit, OnDestroy {
  conversations: AdminChatConversation[] = [];
  filteredConversations: AdminChatConversation[] = [];
  selectedConversation: AdminChatConversation | null = null;
  availableAdmins: AdminChatAdmin[] = [];
  searchTerm = '';
  loading = false;
  loadingAdmins = false;
  errorMessage = '';
  adminError = '';
  private readonly destroy$ = new Subject<void>();

  readonly isAdmin: boolean;
  readonly isSuperAdmin: boolean;

  constructor(
    private readonly adminChatService: AdminChatService,
    private readonly tokenService: TokenService,
  ) {
    this.isAdmin = this.tokenService.hasRole('ADMIN');
    this.isSuperAdmin = this.tokenService.hasRole('SUPER_ADMIN');
  }

  ngOnInit(): void {
    this.loadConversations();
    if (!this.isAdmin) {
      this.loadAdmins();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadConversations() {
    this.loading = true;
    this.adminChatService
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

  loadAdmins() {
    this.loadingAdmins = true;
    this.adminChatService
      .getAdmins()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (admins) => {
          this.availableAdmins = admins;
          this.loadingAdmins = false;
        },
        error: (err) => {
          this.adminError = err.message;
          this.loadingAdmins = false;
        },
      });
  }

  selectConversation(conversation: AdminChatConversation) {
    this.selectedConversation = conversation;
  }

  startConversation(admin: AdminChatAdmin) {
    this.adminError = '';
    this.adminChatService
      .createThread(admin.adminId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (conversation) => {
          const existingIndex = this.conversations.findIndex(
            (item) => item.threadId === conversation.threadId,
          );
          if (existingIndex >= 0) {
            this.conversations[existingIndex] = conversation;
          } else {
            this.conversations = [conversation, ...this.conversations];
          }
          this.applyFilter();
          this.selectedConversation = conversation;
        },
        error: (err) => {
          this.adminError = err.message ?? 'Impossible de démarrer la conversation';
        },
      });
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
