import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { tap } from 'rxjs';

export const logInterceptor: HttpInterceptorFn = (req, next) => {


  const start = Date.now();

  return next(req).pipe(
    tap({
      next: (event) => {
        if (event instanceof HttpResponse) {
          console.log(
            `%c HTTP SUCCESS`,
            "color: #4CAF50",
            `${req.method} ${req.url} (${Date.now() - start}ms)`
          );
        }
      },
      error: (error) => {
        console.error(
          `%c HTTP ERROR \n`,
          "color: #F44336",
          `url: ${error.url}\n
          status: ${error.status}\n
          technicalMessage: ${error.technicalMessage} \n
          message:${error.message} `
        );
      }
    })
  );
};



