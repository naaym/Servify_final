import { inject, Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { TokenService } from '../../../core/services/token.service';
import { LoginRequest, LoginResponse } from '../models/login.models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  tokenservice = inject(TokenService);
  http = inject(Http);

  login(credentials: LoginRequest) {
    return this.http.post<LoginResponse>(`${API_ENDPOINTS.AUTH.LOGIN}`, credentials).pipe(
      tap((res) => {
        this.tokenservice.saveAccessToken(res.access_token);
        this.tokenservice.saveUserId(res.id);
      }),
    );
  }
  logout(){
    this.tokenservice.clearTokens();

  }

  getIdFromLocalStorage() {
    return this.tokenservice.getUserId();
  }
}
