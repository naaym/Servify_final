import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AsideDashboardAdmin } from "../../components/aside-dashboard-admin/aside-dashboard-admin.component";
import { AdminService } from '../../services/admin.service';
import { ProviderApplication, ProviderStatus } from '../../models/provider-application.model';
import { AdminDashboardStats } from '../../models/admin-dashboard-stats.model';
import { TokenService } from '../../../../core/services/token.service';
import { AdminRequest, AdminResponse } from '../../models/admin.model';

@Component({
  selector: 'app-dashboard-admin',
  imports: [CommonModule, AsideDashboardAdmin, FormsModule],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.scss',
})
export class DashboardAdmin implements OnInit {
  private readonly adminService = inject(AdminService);
  private readonly tokenService = inject(TokenService);

  dashboardStats?: AdminDashboardStats;
  providerRequests: ProviderApplication[] = [];
  selectedStatus: ProviderStatus | undefined = undefined;
  loadingRequests = false;
  loadingStats = false;
  loadingAdmins = false;
  error?: string;
  adminError?: string;
  selectedProvider?: ProviderApplication;
  activeSection: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins' = 'dashboard';
  isSuperAdmin = false;
  admins: AdminResponse[] = [];
  adminForm: AdminRequest = { name: '', email: '', phone: '', governorate: '', password: '' };
  editingAdminId?: number;
  showAdminModal = false;

  ngOnInit() {
    this.isSuperAdmin = this.tokenService.hasRole('SUPER_ADMIN');
    this.loadDashboardStats();
  }

  refreshData() {
    this.loadDashboardStats();
    if (this.activeSection === 'providers') {
      this.loadProviderRequests(this.selectedStatus);
    }
    if (this.activeSection === 'admins' && this.isSuperAdmin) {
      this.loadAdmins();
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

  switchSection(section: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins') {
    this.activeSection = section;
    this.error = undefined;
    this.adminError = undefined;
    this.showAdminModal = false;

    if (section === 'providers') {
      this.loadProviderRequests(this.selectedStatus);
    }
    if (section === 'admins' && this.isSuperAdmin) {
      this.loadAdmins();
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

  loadAdmins() {
    this.loadingAdmins = true;
    this.adminService.getAdmins().subscribe({
      next: (admins) => {
        this.admins = admins;
        this.loadingAdmins = false;
      },
      error: (err) => {
        this.adminError = err.message ?? 'Impossible de charger les administrateurs';
        this.loadingAdmins = false;
      },
    });
  }

  startCreateAdmin() {
    this.adminError = undefined;
    this.editingAdminId = undefined;
    this.adminForm = { name: '', email: '', phone: '', governorate: '', password: '' };
    this.showAdminModal = true;
  }

  startEditAdmin(admin: AdminResponse) {
    this.adminError = undefined;
    this.editingAdminId = admin.id;
    this.adminForm = {
      name: admin.name,
      email: admin.email,
      phone: admin.phone,
      governorate: admin.governorate,
      password: '',
    };
    this.showAdminModal = true;
  }

  closeAdminModal() {
    this.showAdminModal = false;
  }

  saveAdmin() {
    this.adminError = undefined;
    const trimmedPassword = this.adminForm.password?.trim();
    const payload: AdminRequest = {
      name: this.adminForm.name.trim(),
      email: this.adminForm.email.trim(),
      phone: this.adminForm.phone.trim(),
      governorate: this.adminForm.governorate.trim(),
      password: trimmedPassword ? trimmedPassword : undefined,
    };

    if (!payload.name || !payload.email || !payload.phone || !payload.governorate) {
      this.adminError = 'Tous les champs (sauf mot de passe en édition) sont requis.';
      return;
    }

    const request$ = this.editingAdminId
      ? this.adminService.updateAdmin(this.editingAdminId, payload)
      : this.adminService.createAdmin(payload);

    this.loadingAdmins = true;
    request$.subscribe({
      next: () => {
        this.startCreateAdmin();
        this.closeAdminModal();
        this.loadAdmins();
        this.loadDashboardStats();
      },
      error: (err) => {
        this.adminError = err.message ?? 'Impossible d’enregistrer l’administrateur';
        this.loadingAdmins = false;
      },
    });
  }

  deleteAdmin(id: number) {
    this.loadingAdmins = true;
    this.adminService.deleteAdmin(id).subscribe({
      next: () => {
        this.loadAdmins();
        this.loadDashboardStats();
      },
      error: (err) => {
        this.adminError = err.message ?? 'Suppression impossible';
        this.loadingAdmins = false;
      },
    });
  }
}
