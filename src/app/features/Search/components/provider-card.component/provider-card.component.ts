import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { Provider } from '../../models/provider.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-provider-card',
  imports: [],
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
