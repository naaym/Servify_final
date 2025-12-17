import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Provider } from '../../models/provider.model';

@Component({
  selector: 'app-provider-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './provider-card.component.html',
  styleUrl: './provider-card.component.scss',
})
export class ProviderCardComponent {
  @Input({required:true}) provider!:Provider;
  @Output() toProviderDetails = new EventEmitter<number>();
  @Output() toReserver = new EventEmitter<number>();

  onClickVoirProfil(id:number){
    this.toProviderDetails.emit(id);
  }
  onClickReserver(id:number){
    this.toReserver.emit(id)
  }





}
