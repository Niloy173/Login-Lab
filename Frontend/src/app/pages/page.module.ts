import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { LayoutModule } from '../layout/layout.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { PageRoutingModule } from './page-routing.module';

@NgModule({
  declarations: [DashboardComponent, HomeComponent],
  imports: [CommonModule, LayoutModule, PageRoutingModule],
})
export class PageModule {}
