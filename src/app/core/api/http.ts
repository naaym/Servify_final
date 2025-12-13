import { HttpClient } from '@angular/common/http';
import { Injectable,inject } from '@angular/core';
import { API_ENDPOINTS } from './endpoints';

@Injectable({
  providedIn: 'root'
})
export class Http {
  BASE = API_ENDPOINTS.BASE;
  http = inject(HttpClient);

  private buildUrl(endpoint: string): string {
    const normalized = endpoint.startsWith('/') ? endpoint.substring(1) : endpoint;
    return `${this.BASE}/${normalized}`;
  }

  post<T>(endpoint: string, body: any) {
    return this.http.post<T>(this.buildUrl(endpoint), body);
  }

  get<T>(endpoint: string, options?: any) {
    return this.http.get<T>(this.buildUrl(endpoint), { params: options });
  }

  getAll<T>(endpoint: string) {
    return this.http.get<T[]>(this.buildUrl(endpoint));
  }

  getOne<T>(endpoint: string, id: number) {
    return this.http.get<T>(this.buildUrl(`${endpoint}/${id}`));
  }

  put<T>(endpoint: string, id: number, body: any) {
    return this.http.put<T>(this.buildUrl(`${endpoint}/${id}`), body);
  }

  patch<T>(endpoint: string, body: any) {
    return this.http.patch<T>(this.buildUrl(endpoint), body);
  }

  delete<T>(endpoint: string, id: number) {
    return this.http.delete<T>(this.buildUrl(`${endpoint}/${id}`));
  }
}
