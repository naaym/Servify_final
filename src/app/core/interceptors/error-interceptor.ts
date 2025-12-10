import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ShowMessageService } from '../../shared/services/showmessage.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageservice=inject(ShowMessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      const normalized = {
        message: mapStatusToMessage(error.status),
        status:error.status,
        url:error.url,
        technicalMessage:error.message,

      };
      messageservice.show("error",normalized.message)

      return throwError(() => normalized);
    })
  );
};

function mapStatusToMessage(status: number): string {
  switch (status) {
    case 0:
      return "Connexion impossible. Vérifiez votre réseau.";
    case 404:
      return "Service temporairement indisponible.";
    case 500:
      return "Un problème interne est survenu.";
    default:
      return "Une erreur est survenue. Veuillez réessayer.";
  }
}



