import { Component, Input } from '@angular/core';

@Component({
  selector: 'card-dashboard-admin',
  imports: [],
  templateUrl: './card-dashboard.html',
  styleUrl: './card-dashboard.scss',
})
export class CardDashboardAdmin {
@Input({required:true}) title!:string;
@Input({required:true}) quantity!:string;

}
