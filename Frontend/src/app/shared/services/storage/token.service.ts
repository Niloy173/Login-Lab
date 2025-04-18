import { Inject, Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { APP_SERVICE_CONFIG } from '../injection/appConfig.service';
import { AppConfig } from '../interface/AppConfig';

const AUTH_TOKEN_KEY = '_ll_uu_t';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private SECRET_KEY: string;

  constructor(@Inject(APP_SERVICE_CONFIG) appConfigService: AppConfig) {
    this.SECRET_KEY = appConfigService.secretKey;
  }

  saveUser(data: { token: string }): void {
    localStorage.setItem(AUTH_TOKEN_KEY, data.token);
  }

  getUser(): { userid: number; role: string; token: string } | null {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);

    if (!token) return null;

    const user: any = {};
    const decodedTokenInfo = jwtDecode(token);

    const { userid, role, username } = decodedTokenInfo as {
      userid: number;
      role: string;
      username: string;
    };

    user.userid = userid;
    user.role = role;
    user.username = username;
    user.token = token;

    return user;
  }

  getToken(): string | null {
    const user = this.getUser();
    console.log('getToken', user);
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
    localStorage.removeItem(AUTH_TOKEN_KEY);
  }

  // Optional: check if user is logged in
  isLoggedIn(): boolean {
    return !!this.getUser();
  }
}
