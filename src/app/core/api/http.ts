import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_ENDPOINTS } from './endpoints';

type QueryParams = HttpParams | Record<string, string | number | boolean>;

type ParamsOptions = { params?: QueryParams };

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

  get<T>(endpoint: string, options?: QueryParams | ParamsOptions) {
    const normalizedOptions = this.normalizeOptions(options);

    return this.http.get<T>(this.buildUrl(endpoint), {
      ...normalizedOptions,
      observe: 'body' as const,
    });
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

  private normalizeOptions(options?: QueryParams | ParamsOptions): ParamsOptions {
    if (!options) {
      return {};
    }

    if (this.isParamsOptions(options)) {
      return options.params ? { params: options.params } : {};
    }

    return { params: options };
  }

  private isParamsOptions(options: unknown): options is ParamsOptions {
    return typeof options === 'object' && options !== null && 'params' in options;
  }
}
