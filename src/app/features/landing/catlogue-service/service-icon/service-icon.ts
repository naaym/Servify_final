import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-service-icon',
  imports: [],
  templateUrl: './service-icon.html',
  styleUrl: './service-icon.scss',
})
export class ServiceIcon {
  @Input({required:true}) image!:{src:string;alt:string};
  @Input({required:true}) titre!:string;


}
