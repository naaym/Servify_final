import { Component } from '@angular/core';
import { AsideDashboardAdmin } from "../../components/aside-dashboard-admin/aside-dashboard-admin.component";
import { CardDashboardAdmin } from '../../components/card-dashboard/card-dashboard.component';
import { ChartDashboardAdmin } from '../../components/chart-dashboard-admin/chart-dashboard-admin.component';

@Component({
  selector: 'app-dashboard-admin',
  imports: [AsideDashboardAdmin,CardDashboardAdmin,ChartDashboardAdmin],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.scss',
})
export class DashboardAdmin {

}
