import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-city',
  imports: [],
  templateUrl: './city.modal.html',
  styleUrl: './city.modal.scss',
})
export class CityModal {
   @Input({required:true}) open:boolean=false;
   @Input({required:true}) serviceName!:string|null;
  @Output() close = new EventEmitter();
  @Output() citySelected = new EventEmitter<string>();
  @Output() retour = new EventEmitter();

   cities = ['Tunisie', 'France', 'Canada', 'Belgique', 'Suisse'];

}
