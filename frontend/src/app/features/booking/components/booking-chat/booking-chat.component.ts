import { CommonModule, DatePipe } from '@angular/common';
import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { TokenService } from '../../../../core/services/token.service';
import { ChatMessage, ChatMessageRequest } from '../../models/chat-message.model';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-booking-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './booking-chat.component.html',
  styleUrl: './booking-chat.component.scss',
})
export class BookingChatComponent implements OnInit, OnDestroy {
  @Input({ required: true }) bookingId!: number;
  @Input() theme: 'light' | 'dark' = 'light';
  @ViewChild('messagesContainer') messagesContainer?: ElementRef<HTMLDivElement>;

  messages: ChatMessage[] = [];
  newMessage = '';
  loading = false;
  errorMessage = '';
  private readonly destroy$ = new Subject<void>();
  private readonly currentUserId: number | null;

  constructor(
    private readonly chatService: ChatService,
    private readonly tokenService: TokenService,
  ) {
    const storedId = this.tokenService.getUserId();
    this.currentUserId = storedId ? Number(storedId) : null;
  }

  ngOnInit(): void {
    if (!this.bookingId) {
      this.errorMessage = 'Réservation invalide.';
      return;
    }

    this.loading = true;
    this.chatService.getHistory(this.bookingId).subscribe({
      next: (messages) => {
        this.messages = messages;
        this.loading = false;
        this.scrollToBottom();
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      },
    });

    this.chatService
      .connect(this.bookingId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (message) => {
          this.messages = [...this.messages, message];
          this.scrollToBottom();
        },
        error: (err) => {
          this.errorMessage = err.message;
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
    if (!trimmed) {
      return;
    }
    const request: ChatMessageRequest = {
      bookingId: this.bookingId,
      content: trimmed,
    };
    this.chatService.sendMessage(request);
    this.newMessage = '';
  }

  isOwnMessage(message: ChatMessage): boolean {
    return this.currentUserId !== null && message.senderId === this.currentUserId;
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
