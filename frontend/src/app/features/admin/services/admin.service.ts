import { inject, Injectable } from '@angular/core';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { AdminRequest, AdminResponse } from '../models/admin.model';
import { AdminDashboardStats } from '../models/admin-dashboard-stats.model';
import { ProviderApplication, ProviderStatus } from '../models/provider-application.model';
import { ProviderRevenueSummary } from '../models/provider-revenue-summary.model';
import { ClientResponse } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(Http);

  getAdmins() {
    return this.http.getAll<AdminResponse>(API_ENDPOINTS.ADMIN.BASE);
  }

  createAdmin(payload: AdminRequest) {
    return this.http.post<AdminResponse>(API_ENDPOINTS.ADMIN.BASE, payload);
  }

  updateAdmin(id: number, payload: AdminRequest) {
    return this.http.put<AdminResponse>(API_ENDPOINTS.ADMIN.BASE, id, payload);
  }

  deleteAdmin(id: number) {
    return this.http.delete<void>(API_ENDPOINTS.ADMIN.BASE, id);
  }

  getDashboardStats() {
    return this.http.get<AdminDashboardStats>(API_ENDPOINTS.ADMIN.STATS);
  }

  getProviderRequests(status?: ProviderStatus) {
    const params = status ? { status } : undefined;
    return this.http.get<ProviderApplication[]>(API_ENDPOINTS.ADMIN.PROVIDER_REQUESTS, params);
  }

  getProviderRevenueSummary() {
    return this.http.get<ProviderRevenueSummary[]>(API_ENDPOINTS.PAYMENTS.SUPER_ADMIN_SUMMARY);
  }

  decideProvider(providerId: number, status: ProviderStatus) {
    return this.http.patch<ProviderApplication>(
      API_ENDPOINTS.ADMIN.PROVIDER_STATUS(providerId),
      { status }
    );
  }

  deleteProvider(providerId: number) {
    return this.http.delete<void>(API_ENDPOINTS.ADMIN.PROVIDERS, providerId);
  }

  getClients() {
    return this.http.getAll<ClientResponse>(API_ENDPOINTS.ADMIN.CLIENTS);
  }

  deleteClient(clientId: number) {
    return this.http.delete<void>(API_ENDPOINTS.ADMIN.CLIENTS, clientId);
  }
}
