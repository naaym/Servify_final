import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { ChatNotificationService } from '../../../chat/services/chat-notification.service';

@Component({
  selector: 'app-aside',
  imports: [CommonModule, RouterModule],
  templateUrl: './aside.component.html',
  styleUrl: './aside.component.scss',
})
export class AsideComponent implements OnInit {
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly chatNotificationService = inject(ChatNotificationService);
    readonly unreadCount$ = this.chatNotificationService.unreadCount$;

    ngOnInit(): void {
      this.chatNotificationService.startPolling();
    }

    logout(){
    this.authService.logout();
    this.router.navigate(['/'])
  }

}
