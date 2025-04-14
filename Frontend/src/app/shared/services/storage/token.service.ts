import { Inject, Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';
import { APP_SERVICE_CONFIG } from '../injection/appConfig.service';
import { AppConfig } from '../interface/AppConfig';

const SAVED_KEY = 'auth-data';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private SECRET_KEY: string;

  constructor(@Inject(APP_SERVICE_CONFIG) appConfigService: AppConfig) {
    this.SECRET_KEY = appConfigService.secretKey;
  }

  saveUser(data: { userId: number; role: string; token: string }): void {
    const encrypted = CryptoJS.AES.encrypt(
      JSON.stringify(data),
      this.SECRET_KEY
    ).toString();

    localStorage.setItem(SAVED_KEY, encrypted);
  }

  getUser(): { userId: number; role: string; token: string } | null {
    const encrypted = localStorage.getItem(SAVED_KEY);
    if (!encrypted) return null;

    try {
      const decrypted = CryptoJS.AES.decrypt(
        encrypted,
        this.SECRET_KEY
      ).toString(CryptoJS.enc.Utf8);
      return JSON.parse(decrypted);
    } catch (error) {
      return null;
    }
  }

  getToken(): string | null {
    const user = this.getUser();
    return user?.token || null;
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
    localStorage.removeItem(SAVED_KEY);
  }

  // Optional: check if user is logged in
  isLoggedIn(): boolean {
    return !!this.getUser();
  }
}
