import { CommonModule, DatePipe } from '@angular/common';
import {
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { TokenService } from '../../../../core/services/token.service';
import {
  AdminChatMessage,
  AdminChatMessageRequest,
} from '../../models/admin-chat.model';
import { AdminChatService } from '../../services/admin-chat.service';

@Component({
  selector: 'app-admin-chat-thread',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './admin-chat-thread.component.html',
  styleUrl: './admin-chat-thread.component.scss',
})
export class AdminChatThreadComponent implements OnInit, OnChanges, OnDestroy {
  @Input({ required: true }) threadId!: number;
  @ViewChild('messagesContainer')
  messagesContainer?: ElementRef<HTMLDivElement>;

  messages: AdminChatMessage[] = [];
  newMessage = '';
  loading = false;
  errorMessage = '';

  private destroy$ = new Subject<void>();
  private readonly currentUserId: number | null;
  private initialized = false;

  constructor(
    private readonly adminChatService: AdminChatService,
    private readonly tokenService: TokenService,
  ) {
    const storedId = this.tokenService.getUserId();
    this.currentUserId = storedId ? Number(storedId) : null;
  }

  ngOnInit(): void {
    this.initialized = true;

    if (!this.threadId) {
      this.errorMessage = 'Conversation invalide.';
      return;
    }

    this.startConversation(this.threadId);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.initialized) return;

    if (changes['threadId']) {
      const newId = changes['threadId'].currentValue as number;
      const oldId = changes['threadId'].previousValue as number;

      if (!newId || newId === oldId) return;

      this.startConversation(newId);
    }
  }

  private startConversation(threadId: number) {
    this.destroy$.next();
    this.adminChatService.disconnect();

    this.messages = [];
    this.newMessage = '';
    this.errorMessage = '';
    this.loading = true;

    this.destroy$.complete();
    this.destroy$ = new Subject<void>();

    this.adminChatService
      .getMessages(threadId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (messages) => {
          this.messages = messages;
          this.loading = false;
          this.scrollToBottom();
        },
        error: (err) => {
          this.errorMessage = err?.message || 'Erreur chargement historique.';
          this.loading = false;
        },
      });

    this.adminChatService
      .connect(threadId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (message) => {
          this.messages = [...this.messages, message];
          this.scrollToBottom();
        },
        error: (err) => {
          this.errorMessage = err?.message || 'Erreur websocket.';
        },
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.adminChatService.disconnect();
  }

  sendMessage() {
    const trimmed = this.newMessage.trim();
    if (!trimmed) return;

    const request: AdminChatMessageRequest = {
      threadId: this.threadId,
      content: trimmed,
    };

    this.adminChatService.sendMessage(request);
    this.newMessage = '';
  }

  isOwnMessage(message: AdminChatMessage): boolean {
    return (
      this.currentUserId !== null && message.senderId === this.currentUserId
    );
  }

  private scrollToBottom() {
    requestAnimationFrame(() => {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop =
          this.messagesContainer.nativeElement.scrollHeight;
      }
    });
  }
}
