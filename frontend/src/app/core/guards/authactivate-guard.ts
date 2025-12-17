import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { inject } from '@angular/core';

export const AuthActivateGuard: CanActivateFn = (_route, _state) => {


    const auth=inject(TokenService);
    const router=inject(Router);
    if(!auth.isLoggedIn()){
      router.navigate(["/client"]);
      return false;
    }
    // methode pour verifiee jeton expiree ou pas

    return true;
  }

