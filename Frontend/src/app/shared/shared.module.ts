import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {
  APP_CONFIG,
  APP_SERVICE_CONFIG,
} from './services/injection/appConfig.service';

@NgModule({
  declarations: [],
  imports: [CommonModule],
  providers: [
    /* injection tokens */
    {
      provide: APP_SERVICE_CONFIG,
      useValue: APP_CONFIG,
    },
  ],
})
export class SharedModule {}
