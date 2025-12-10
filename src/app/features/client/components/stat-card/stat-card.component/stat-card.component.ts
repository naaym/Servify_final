import { Component, Input } from '@angular/core';

@Component({
  selector: 'stat-card',
  imports: [],
  templateUrl: './stat-card.component.html',
  styleUrl: './stat-card.component.scss',
})
export class StatCardComponent {
@Input({required:true}) totalRequests!:number;
@Input({required:true}) color!:string;
@Input({required:true}) status!:string;
}
