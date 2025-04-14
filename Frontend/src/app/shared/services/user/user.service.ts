import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { User } from 'src/app/auth/model/User';
import { APP_SERVICE_CONFIG } from '../injection/appConfig.service';
import { AppConfig } from '../interface/AppConfig';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private http: HttpClient,
    @Inject(APP_SERVICE_CONFIG) private appServiceConfig: AppConfig
  ) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.appServiceConfig.apiUrl}/user/all`);
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
