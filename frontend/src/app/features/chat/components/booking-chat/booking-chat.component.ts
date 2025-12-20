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
  ChatMessage,
  ChatMessageRequest,
} from '../../models/chat-message.model';
import { ChatNotificationService } from '../../services/chat-notification.service';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-booking-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './booking-chat.component.html',
  styleUrl: './booking-chat.component.scss',
})
export class BookingChatComponent implements OnInit, OnChanges, OnDestroy {
  @Input({ required: true }) bookingId!: number;
  @Input() theme: 'light' | 'dark' = 'light';
  @ViewChild('messagesContainer')
  messagesContainer?: ElementRef<HTMLDivElement>;

  messages: ChatMessage[] = [];
  newMessage = '';
  loading = false;
  errorMessage = '';

  private destroy$ = new Subject<void>();
  private readonly currentUserId: number | null;
  private initialized = false;

  constructor(
    private readonly chatService: ChatService,
    private readonly tokenService: TokenService,
    private readonly chatNotificationService: ChatNotificationService,
  ) {
    const storedId = this.tokenService.getUserId();
    this.currentUserId = storedId ? Number(storedId) : null;
  }

  ngOnInit(): void {
    this.initialized = true;

    if (!this.bookingId) {
      this.errorMessage = 'Réservation invalide.';
      return;
    }

    this.startConversation(this.bookingId);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.initialized) return; // ignore avant ngOnInit

    if (changes['bookingId']) {
      const newId = changes['bookingId'].currentValue as number;
      const oldId = changes['bookingId'].previousValue as number;

      if (!newId || newId === oldId) return;

      this.startConversation(newId);
    }
  }

  private startConversation(bookingId: number) {
    this.destroy$.next();
    this.chatService.disconnect();

    this.messages = [];
    this.newMessage = '';
    this.errorMessage = '';
    this.loading = true;

    this.destroy$.complete();
    this.destroy$ = new Subject<void>();

    this.chatService
      .getHistory(bookingId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (messages) => {
          this.messages = messages;
          this.loading = false;
          this.scrollToBottom();
          this.chatNotificationService.markConversationAsRead(bookingId);
        },
        error: (err) => {
          this.errorMessage = err?.message || 'Erreur chargement historique.';
          this.loading = false;
        },
      });

    this.chatService
      .connect(bookingId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (message) => {
          this.messages = [...this.messages, message];
          this.scrollToBottom();
          this.chatNotificationService.markConversationAsRead(bookingId);
        },
        error: (err) => {
          this.errorMessage = err?.message || 'Erreur websocket.';
        },
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.chatService.disconnect();
  }

  sendMessage() {
    const trimmed = this.newMessage.trim();
    if (!trimmed) return;

    const request: ChatMessageRequest = {
      bookingId: this.bookingId,
      content: trimmed,
    };

    this.chatService.sendMessage(request);
    this.newMessage = '';
  }

  isOwnMessage(message: ChatMessage): boolean {
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
