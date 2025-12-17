import { CanMatchFn} from '@angular/router';
import { TokenService } from '../services/token.service';
import { inject } from '@angular/core';

export const roleMatchGuard = (allowedRules: string[]): CanMatchFn => {
  return (_route,_segment) => {

    const auth = inject(TokenService);

    const userRoles = auth.getRoles();
    return userRoles.some((role) => allowedRules.includes(role));
  };
};
