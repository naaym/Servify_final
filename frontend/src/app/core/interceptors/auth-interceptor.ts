import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';
import { EXCLUDED_URLS } from '../api/excludedurls';

 export const authInterceptor: HttpInterceptorFn = (req,next) => {
  const tokenservice = inject(TokenService);
  const token = tokenservice.getAccessToken();


    if (EXCLUDED_URLS.some(url=>req.url.includes(url))){
     return next(req)
   }

   if (token) {
     req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  console.log('requete intercepted',req.url)

  return next(req);
};
