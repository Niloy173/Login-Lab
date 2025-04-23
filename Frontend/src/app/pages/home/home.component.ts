import { Component, SkipSelf } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/shared/services/storage/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  // Nothing to do here right now, but this is the main component for the home page

  constructor(
    private router: Router,
    @SkipSelf() private tokenService: TokenService
  ) {}

  redirectProfile(): void {
    if (this.tokenService.isLoggedIn()) {
      this.router.navigate(['/profile']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
