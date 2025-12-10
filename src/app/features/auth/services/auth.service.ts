import {inject, Injectable } from '@angular/core';
import { TokenService } from '../../../core/services/token.service';
import { tap } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/login.models';
import { STORAGE } from '../../../core/constants/storage.constants';
import { UserService } from '../../../core/services/user.service';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { Http } from '../../../core/api/http';


@Injectable({
  providedIn:'root'
})
export class  AuthService{

    tokenservice=inject(TokenService);
    userservice=inject(UserService)
    http=inject(Http);

  login(credentials:LoginRequest){

    return this.http.post<LoginResponse>(`${API_ENDPOINTS.AUTH.LOGIN}`,credentials,).pipe(
      tap(res=>{
        localStorage.setItem(STORAGE.ACCESS_TOKEN, res.access_token); // me7atinehouch fel partie .subscribe() khater le component ne gere pas le localstorage et stockage du token , teba3 service
        localStorage.setItem(STORAGE.ACCESS_TOKEN, res.id.toString());
      }
    ))

  }

  getIdFromLocalStorage(){
    return localStorage.getItem("id");
  }






}
