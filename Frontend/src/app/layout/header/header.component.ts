import { Component } from '@angular/core';
import { Router } from '@angular/router';
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

  constructor(private router: Router, private tokenService: TokenService) {
    this.AUTH_STATUS = this.tokenService.isLoggedIn()
      ? 'AUTHORIZED'
      : 'UNAUTHORIZED';
  }

  logout() {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
    // window.location.reload();
  }
}
