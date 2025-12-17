import { Component ,inject} from '@angular/core';
import { ShowMessageService } from '../services/showmessage.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast',
  imports: [CommonModule],
  templateUrl: './toast.html',
  styleUrl: './toast.scss',
})
export class Toast {
  messageService = inject(ShowMessageService);

}
