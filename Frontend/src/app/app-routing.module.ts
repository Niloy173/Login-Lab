import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./auth/components/login/login.module').then((m) => m.LoginModule),
    data: { title: 'Login' },
  },

  {
    path: 'register',
    loadChildren: () =>
      import('./auth/components/register/register.module').then(
        (m) => m.RegisterModule
      ),
    data: { title: 'Register' },
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
