import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router } from '@angular/router';
import { TokenService } from 'src/app/shared/services/storage/token.service';

export const canActivateGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (tokenService.isLoggedIn()) {
    return true;
  } else {
    router.navigate(['/login'], {});
    /**
     * {
      queryParams: {
        returnUrl: state.url,
      },
    }
     */
    return false;
  }
};

export const canActivateChildGuard: CanActivateChildFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (tokenService.isLoggedIn()) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
