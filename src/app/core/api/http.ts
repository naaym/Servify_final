import { HttpClient } from '@angular/common/http';
import { Injectable,inject } from '@angular/core';
import { API_ENDPOINTS } from './endpoints';

@Injectable({
  providedIn: 'root'
})
export class Http {
  BASE=API_ENDPOINTS.BASE;
  http=inject(HttpClient);


  post<T>(endpoint : string ,body:any){
    return this.http.post<T>(`${this.BASE}${endpoint}`,body);
  }
  get<T>(endpoint:string,options?:any){
    return this.http.get<T>(`${this.BASE}${endpoint}`,{params:options})
  }


  getAll<T>(endpoint:string){
    return this.http.get<T[]>(`${this.BASE}/${endpoint}`);

  }


  getOne<T>(endpoint:string,id:number){
    return this.http.get<T>(`${this.BASE}${endpoint}/${id}`)
  }


  put<T>(endpoint: string, id:  number, body: any) {
    return this.http.put<T>(`${this.BASE}/${endpoint}/${id}`, body);
  }


  delete<T>(endpoint:string,id:number){
    return this.http.delete<T>(`${this.BASE}/${endpoint}/${id}`)
  }



}
