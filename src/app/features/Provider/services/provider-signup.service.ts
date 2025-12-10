import { inject, Injectable } from "@angular/core";
import { Http } from "../../../core/api/http";
import { ProviderSignUpRequest, ProviderSignUpResponse } from "../models/provider-signup.model";
import { API_ENDPOINTS } from "../../../core/api/endpoints";
import { catchError, throwError } from "rxjs";

@Injectable({providedIn:"root"})
export class providerSignUpService{

private readonly http=inject(Http);



signUp(data:ProviderSignUpRequest){
  const formData=new FormData()
   formData.append('name', data.name);
    formData.append('email', data.email);
    formData.append('password', data.password);
    formData.append('age', data.age.toString()); // lezem nconvertouha apres khater formdata n'accepte pas number
    formData.append('governorate', data.governorate);
    formData.append('delegation', data.delegation);
    formData.append('phone', data.phone);

    formData.append('cin', data.cin);
    formData.append('cv', data.cv);
    formData.append('diplome', data.diplome);
 return this.http.post<ProviderSignUpResponse>(`${API_ENDPOINTS.PROVIDER.BASE}${API_ENDPOINTS.PROVIDER.REGISTER}`,formData).
  pipe(catchError((err)=>{const normalized={message:err.message};
  return throwError(()=>normalized)}))
}





}
