import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ShowMessageService } from '../../shared/services/showmessage.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageservice=inject(ShowMessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      const normalized = {
        message: mapStatusToMessage(error.status , error.error),
        status:error.status,
        url:error.url,
      };
      messageservice.show("error",normalized.message)

      return throwError(() => normalized);

    })
  );
};

function mapStatusToMessage(status: number, message : string): string {

  switch (status) {
    case 0:
      return `${message}`;
    case 404:
      return `${message}`;
    case 500:
      return `${message}`;
    case 400:
      return `${message}`;
    default:
      return `${message}`;
  }
}



