import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type MessageType = 'success' | 'error' | 'info' | 'warning';

export interface AppMessage {
  type: MessageType;
  text: string;
}

@Injectable({ providedIn: 'root' })
export class ShowMessageService {
  private messageSubject = new BehaviorSubject<AppMessage | null>(null);
  message$ = this.messageSubject.asObservable();

  show(type: MessageType, text: string) {
    this.messageSubject.next({ type, text });

    setTimeout(() => this.clear(), 2000);
  }

  clear() {
    this.messageSubject.next(null);
  }
}
