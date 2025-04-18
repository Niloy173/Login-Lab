import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canActivateGuard } from 'src/app/core/guards/auth.guard';
import { HomeComponent } from './home.component';

const routes: Routes = [
  { path: 'home', redirectTo: '', pathMatch: 'full' },
  {
    path: '',
    component: HomeComponent,
    data: { title: 'home' },
    canActivate: [canActivateGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
