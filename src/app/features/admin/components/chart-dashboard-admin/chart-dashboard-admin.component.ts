import { Component,Input } from '@angular/core';

@Component({
  selector: 'chart-dashboard-admin',
  imports: [],
  templateUrl: './chart-dashboard-admin.html',
  styleUrl: './chart-dashboard-admin.scss',
})
export class ChartDashboardAdmin {
@Input({required:true}) title!:string;
}
