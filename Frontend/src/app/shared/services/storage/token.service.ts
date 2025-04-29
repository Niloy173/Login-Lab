import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { APP_SERVICE_CONFIG } from '../injection/appConfig.service';
import { AppConfig } from '../interface/AppConfig';

// const AUTH_TOKEN_KEY = '_ll_uu_t';
const AUTH_USER_ID_KEY = 'auth_user_id';
const AUTH_USER_ROLE_KEY = 'auth_user_role';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private authStatusSubject = new BehaviorSubject<boolean>(false);
  authStatus$ = this.authStatusSubject.asObservable();

  constructor(@Inject(APP_SERVICE_CONFIG) appConfigService: AppConfig) {}

  saveUser(data: { userid: number; role: string; token: string }): void {
    this.authStatusSubject.next(true);

    localStorage.setItem(AUTH_USER_ID_KEY, data.userid.toString());
    localStorage.setItem(AUTH_USER_ROLE_KEY, data.role);
    // localStorage.setItem(AUTH_TOKEN_KEY, data.token);
  }

  getUser(): { userid: number; role: string; token: string } | null {
    const l_userid = localStorage.getItem(AUTH_USER_ID_KEY);
    const l_role = localStorage.getItem(AUTH_USER_ROLE_KEY);
    // const token = localStorage.getItem(AUTH_TOKEN_KEY);

    if (!l_userid || !l_role) return null;

    const user: any = {};
    // const decodedTokenInfo = jwtDecode(token);

    // const { userid, role, username } = decodedTokenInfo as {
    //   userid: number;
    //   role: string;
    //   username: string;
    // };

    user.userid = l_userid;
    user.role = l_role;
    // user.username = username;
    // user.token = token;

    return user;
  }

  getToken(): string | null {
    const user = this.getUser();
    // console.log('getToken', user);
    return user?.token || null;
  }

  getUserId(): number | null {
    const user = this.getUser();
    return user?.userid || null;
  }

  getUserRole(): string | null {
    const user = this.getUser();
    return user?.role || null;
  }

  setIsLoggedIn(isLoggedIn: boolean): void {
    this.authStatusSubject.next(isLoggedIn);
  }

  // Save token to localStorage
  // saveToken(token: string): void {
  //   localStorage.setItem(TOKEN_KEY, token);
  // }

  // Get token from localStorage
  // getToken(): string | null {
  //   return localStorage.getItem(TOKEN_KEY);
  // }

  // Remove token on logout
  // clearToken(): void {
  //   localStorage.removeItem(TOKEN_KEY);
  // }

  removeToken(): void {
    // localStorage.removeItem(AUTH_TOKEN_KEY);
    localStorage.removeItem(AUTH_USER_ID_KEY);
    localStorage.removeItem(AUTH_USER_ROLE_KEY);
  }

  // Optional: check if user is logged in
  isLoggedIn(): boolean {
    // return !!this.getUser();
    return !!this.getUser() && this.authStatusSubject.value;
  }
}
