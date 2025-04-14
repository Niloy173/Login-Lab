import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canActivateGuard } from 'src/app/core/guards/auth.guard';
import { ProfileComponent } from './profile.component';

const routes: Routes = [
  { path: 'profile', redirectTo: '', pathMatch: 'full' },
  {
    path: '',
    component: ProfileComponent,
    data: { title: 'Profile' },
    canActivate: [canActivateGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProfileRoutingModule {}
