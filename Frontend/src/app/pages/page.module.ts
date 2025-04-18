import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { LayoutModule } from '../layout/layout.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PageRoutingModule } from './page-routing.module';

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, LayoutModule, PageRoutingModule],
})
export class PageModule {}
