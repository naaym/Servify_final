import { inject, Injectable } from '@angular/core';
import { Http } from '../../../core/api/http';
import { SearchProviderResult, SearchProviderRequest } from '../models/relsult-search.model';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class SearchProviderService {
  http=inject(HttpClient);

  searchProvider(params: SearchProviderRequest): Observable<SearchProviderResult> {
    const queryParams = this.buildQueryParams(params);
    return this.http
      .get<SearchProviderResult>(
        `${API_ENDPOINTS.BASE}/${API_ENDPOINTS.PROVIDER.SEARCH}`,
        { params: queryParams }
      );
  }
  buildQueryParams(params: SearchProviderRequest) {
    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        httpParams = httpParams.set(key, String(value) );
      }
    });
    return httpParams;
  }
}
