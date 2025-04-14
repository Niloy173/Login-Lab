import { InjectionToken } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppConfig } from '../interface/AppConfig';

export const APP_SERVICE_CONFIG = new InjectionToken<AppConfig>('app.config');

export const APP_CONFIG: AppConfig = {
  apiUrl: environment.apiUrl,
  secretKey: environment.secret_key,
};
