import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';

@Injectable({ providedIn: 'root' })
export class SearchOptionsService {
  private http = inject(HttpClient);

  getAvailableServices(): Observable<string[]> {
    return this.http.get<string[]>(`${API_ENDPOINTS.BASE}/${API_ENDPOINTS.PROVIDER.SEARCH_OPTIONS.SERVICES}`);
  }

  getAvailableGovernorates(serviceCategory?: string): Observable<string[]> {
    const normalizedService = serviceCategory?.trim();
    const params = normalizedService ? new HttpParams().set('serviceCategory', normalizedService) : undefined;
    return this.http.get<string[]>(
      `${API_ENDPOINTS.BASE}/${API_ENDPOINTS.PROVIDER.SEARCH_OPTIONS.GOVERNORATES}`,
      { params }
    );
  }
}
