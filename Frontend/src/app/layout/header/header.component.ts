import { Component, SkipSelf } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { AuthService } from 'src/app/auth/services/auth.service';
import { TokenService } from 'src/app/shared/services/storage/token.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  APP_NAME: string = 'LOGIN LAB';
  AUTH_STATUS: 'AUTHORIZED' | 'UNAUTHORIZED' = 'UNAUTHORIZED';
  showDropdown = false;

  constructor(
    private router: Router,
    private tokenService: TokenService,
    @SkipSelf() private authService: AuthService
  ) {
    this.AUTH_STATUS = this.tokenService.isLoggedIn()
      ? 'AUTHORIZED'
      : 'UNAUTHORIZED';
  }

  logout() {
    this.authService
      .logOut()
      .pipe(
        tap((res) => {
          console.log(res);
          this.tokenService.removeToken(); // Remove token on logout
          this.router.navigate(['/login']);
        })
      )
      .subscribe();

    // window.location.reload();
  }
}
