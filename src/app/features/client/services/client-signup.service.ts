import { inject, Injectable } from '@angular/core';
import { ClientSignUpRequest,ClientSignUpResponse } from '../models/client-signup.model';
import { catchError, Observable, throwError } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { Http } from '../../../core/api/http';

@Injectable({
  providedIn: 'root'
})
export class ClientSignUpService {
  private readonly http=inject(Http);
  register(data:ClientSignUpRequest){
  return this.http.post<ClientSignUpResponse>(API_ENDPOINTS.CLIENT.REGISTER,data)


}}
