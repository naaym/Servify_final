import { Injectable } from '@angular/core';
import { STORAGE } from '../constants/storage.constants';
import { jwtDecode } from 'jwt-decode';

@Injectable({ providedIn: 'root' })
export class TokenService {
  private tokenKey = STORAGE.ACCESS_TOKEN;
  private userIdKey = STORAGE.USER_ID;


  saveAccessToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  saveUserId(id: number) {
    localStorage.setItem(this.userIdKey, id.toString());
  }


  clearTokens() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userIdKey);
  }


  getAccessToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUserId(): string | null {
    return localStorage.getItem(this.userIdKey);
  }


  isLoggedIn(): boolean {
    return !! this.getAccessToken();
  }


  decodeToken(): any | null {

    const token = this.getAccessToken();
    if (!token) {
      return null;
    }
    try {
      return jwtDecode(token);
    } catch (err) {
      console.error('invalid token ', err);
      return null;
    }
  }

  getRoles():string[]{
    const payload= this.decodeToken();
    if (!payload) return []
    if(payload.roles){
      return payload.roles
    }
    if(payload.role){
      return [payload.role]
    }
    return  []

  }

  hasRole(role:string):boolean{
    return this.getRoles().includes(role);
  }
  //isTokenExpired()

}
