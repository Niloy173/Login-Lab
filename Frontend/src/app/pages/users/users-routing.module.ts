import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canActivateGuard } from 'src/app/core/guards/auth.guard';
import { UsersComponent } from './users.component';

const routes: Routes = [
  {
    path: 'users',
    redirectTo: '',
    pathMatch: 'full',
  },

  {
    path: '',
    component: UsersComponent,
    data: { title: 'Users' },
    canActivate: [canActivateGuard],
  },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsersRoutingModule {}
