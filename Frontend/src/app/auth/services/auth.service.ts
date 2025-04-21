import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { APP_SERVICE_CONFIG } from 'src/app/shared/services/injection/appConfig.service';
import { AppConfig } from 'src/app/shared/services/interface/AppConfig';
import { User } from '../model/User';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(
    private http: HttpClient,
    @Inject(APP_SERVICE_CONFIG) private appConfigService: AppConfig
  ) {}

  logOut(): Observable<any> {
    return this.http
      .get<any>(`${this.appConfigService.apiUrl}/auth/logout`)
      .pipe(catchError(this.handleError));
  }

  profile(): Observable<any> {
    return this.http
      .get<any>(`${this.appConfigService.apiUrl}/auth/profile`)
      .pipe(catchError(this.handleError));
  }

  register(user: User): Observable<any> {
    return this.http
      .post<any>(`${this.appConfigService.apiUrl}/auth/register`, user)
      .pipe(catchError(this.handleError));
  }

  login(user: { email: string; password: string }) {
    return this.http
      .post(`${this.appConfigService.apiUrl}/auth/login`, user, {
        headers: this.headers,
      })
      .pipe(catchError(this.handleError));
  }

  private handleError(errorResponse: HttpErrorResponse) {
    if (errorResponse.error instanceof ErrorEvent) {
      console.error('Client Side Error: ', errorResponse.error);
    } else {
      console.error('Server Side Error: ', errorResponse);
    }
    return throwError(
      'There is a problem with the Service, Please contact with System Administrator.'
    );
  }
}
