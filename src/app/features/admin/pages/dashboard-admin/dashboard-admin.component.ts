import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AsideDashboardAdmin } from "../../components/aside-dashboard-admin/aside-dashboard-admin.component";
import { CardDashboardAdmin } from '../../components/card-dashboard/card-dashboard.component';
import { ChartDashboardAdmin } from '../../components/chart-dashboard-admin/chart-dashboard-admin.component';
import { AdminService } from '../../services/admin.service';
import { ProviderApplication, ProviderStatus } from '../../models/provider-application.model';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule, AsideDashboardAdmin,CardDashboardAdmin,ChartDashboardAdmin],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.scss',
})
export class DashboardAdmin implements OnInit {
  private readonly adminService = inject(AdminService);

  providerRequests: ProviderApplication[] = [];
  overviewCounts = { total: 0, pending: 0, accepted: 0, rejected: 0 };
  selectedStatus: ProviderStatus | undefined = 'PENDING';
  loading = false;
  error?: string;

  ngOnInit() {
    this.refreshData();
  }

  refreshData() {
    this.loadProviderRequests(this.selectedStatus);
    this.loadOverview();
  }

  loadOverview() {
    this.adminService.getProviderRequests().subscribe({
      next: (requests) => {
        this.overviewCounts = {
          total: requests.length,
          pending: requests.filter((r) => r.status === 'PENDING').length,
          accepted: requests.filter((r) => r.status === 'ACCEPTED').length,
          rejected: requests.filter((r) => r.status === 'REJECTED').length,
        };
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de charger les statistiques';
      },
    });
  }

  loadProviderRequests(status?: ProviderStatus) {
    this.loading = true;
    this.selectedStatus = status;
    this.adminService.getProviderRequests(status).subscribe({
      next: (requests) => {
        this.providerRequests = requests;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Une erreur est survenue';
        this.loading = false;
      },
    });
  }

  updateStatus(providerId: number, status: ProviderStatus) {
    this.loading = true;
    this.adminService.decideProvider(providerId, status).subscribe({
      next: () => {
        this.refreshData();
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de mettre à jour le statut';
        this.loading = false;
      },
    });
  }
}
