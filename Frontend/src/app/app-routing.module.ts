import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canActivateRedirectGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./auth/components/login/login.module').then((m) => m.LoginModule),
    data: { title: 'Login' },
    canActivate: [canActivateRedirectGuard],
  },

  {
    path: 'register',
    loadChildren: () =>
      import('./auth/components/register/register.module').then(
        (m) => m.RegisterModule
      ),
    data: { title: 'Register' },
    canActivate: [canActivateRedirectGuard],
  },

  {
    path: '',
    loadChildren: () => import('./pages/page.module').then((m) => m.PageModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
