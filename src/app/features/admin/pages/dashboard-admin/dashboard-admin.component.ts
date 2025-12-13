import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AsideDashboardAdmin } from "../../components/aside-dashboard-admin/aside-dashboard-admin.component";
import { AdminService } from '../../services/admin.service';
import { ProviderApplication, ProviderStatus } from '../../models/provider-application.model';
import { AdminDashboardStats } from '../../models/admin-dashboard-stats.model';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule, AsideDashboardAdmin],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.scss',
})
export class DashboardAdmin implements OnInit {
  private readonly adminService = inject(AdminService);

  dashboardStats?: AdminDashboardStats;
  providerRequests: ProviderApplication[] = [];
  selectedStatus: ProviderStatus | undefined = undefined;
  loadingRequests = false;
  loadingStats = false;
  error?: string;
  selectedProvider?: ProviderApplication;
  activeSection: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' = 'dashboard';

  ngOnInit() {
    this.loadDashboardStats();
  }

  refreshData() {
    this.loadDashboardStats();
    if (this.activeSection === 'providers') {
      this.loadProviderRequests(this.selectedStatus);
    }
  }

  loadDashboardStats() {
    this.loadingStats = true;
    this.adminService.getDashboardStats().subscribe({
      next: (stats) => {
        this.dashboardStats = stats;
        this.loadingStats = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de charger les statistiques';
        this.loadingStats = false;
      },
    });
  }

  switchSection(section: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services') {
    this.activeSection = section;
    this.error = undefined;

    if (section === 'providers') {
      this.loadProviderRequests(this.selectedStatus);
    }
  }

  loadProviderRequests(status?: ProviderStatus) {
    this.loadingRequests = true;
    this.selectedStatus = status;
    this.adminService.getProviderRequests(status).subscribe({
      next: (requests) => {
        this.providerRequests = requests;
        this.loadingRequests = false;
      },
      error: (err) => {
        this.error = err.message ?? 'Une erreur est survenue';
        this.loadingRequests = false;
      },
    });
  }

  updateStatus(providerId: number, status: ProviderStatus) {
    this.loadingRequests = true;
    this.adminService.decideProvider(providerId, status).subscribe({
      next: () => {
        this.refreshData();
      },
      error: (err) => {
        this.error = err.message ?? 'Impossible de mettre à jour le statut';
        this.loadingRequests = false;
      },
    });
  }

  viewApplication(provider: ProviderApplication) {
    this.selectedProvider = provider;
  }

  closeApplication() {
    this.selectedProvider = undefined;
  }
}
