import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable } from 'rxjs';
import { TokenService } from 'src/app/shared/services/storage/token.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private tokenService: TokenService, private router: Router) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // const token = this.tokenService.getToken();

    // if (token) {
    request = request.clone({
      // setHeaders: {
      //   Authorization: `Bearer ${token}`,
      // },
      withCredentials: true, // Important for sending cookies
    });
    // }

    return next.handle(request).pipe(
      catchError((error) => {
        if (error.status === 401) {
          // Handle unauthorized error
          console.error('Unauthorized request:', error);
          this.tokenService.removeToken(); // Remove token on 401 error
          this.router.navigate(['/login']); // Redirect to login page
        }
        // } else if (error.status === 403) {
        //   // Handle forbidden error
        //   console.error('Forbidden request:', error);
        //   alert('You do not have permission to access this resource.');
        // } else {
        //   // Handle other errors
        //   console.error('HTTP error:', error);
        //   alert('An error occurred. Please try again later.');
        // }
        throw error; // Rethrow the error to propagate it further
      })
    );
  }
}
