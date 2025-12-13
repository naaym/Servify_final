import { inject, Injectable } from '@angular/core';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { AdminRequest, AdminResponse } from '../models/admin.model';
import { ProviderApplication, ProviderStatus } from '../models/provider-application.model';

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

  getProviderRequests(status?: ProviderStatus) {
    const params = status ? { status } : undefined;
    return this.http.get<ProviderApplication[]>(API_ENDPOINTS.ADMIN.PROVIDER_REQUESTS, params);
  }

  decideProvider(providerId: number, status: ProviderStatus) {
    return this.http.patch<ProviderApplication>(
      API_ENDPOINTS.ADMIN.PROVIDER_STATUS(providerId),
      { status }
    );
  }
}
