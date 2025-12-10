import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-service',
  imports: [],
  templateUrl: './service.modal.html',
  styleUrl: './service.modal.scss',
})
export class ServiceModal {
  @Input({required:true}) open:boolean=false;
  @Output() close = new EventEmitter();
  @Output() selectService = new EventEmitter<string>();


  services = [
    'Développement_Web',
    'Développement_Mobile',
    'Design_UI/UX',
    'Marketing_Digital',
    'Maintenance_Informatique'
  ];


}
