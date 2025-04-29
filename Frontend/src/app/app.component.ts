import { Component, OnInit, SkipSelf } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { catchError, filter, finalize, map, mergeMap, tap } from 'rxjs';
import { AuthService } from './auth/services/auth.service';
import { TokenService } from './shared/services/storage/token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Frontend';

  constructor(
    private titleService: Title,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    @SkipSelf() private authService: AuthService,
    @SkipSelf() private tokenService: TokenService
  ) {}
  ngOnInit(): void {
    this.checkAuthStatus();

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => {
          let route = this.activatedRoute;
          while (route?.firstChild) {
            route = route.firstChild;
          }
          return route;
        }),
        mergeMap((route) => route?.data || [])
      )
      .subscribe((data) => {
        const title = data['title'] || 'LOGIN LAB';
        this.titleService.setTitle(title);
      });
  }

  checkAuthStatus(): void {
    this.authService
      .checkAuthStatus()
      .pipe(
        catchError((error) => {
          // console.error('Error checking auth status:', error);
          this.tokenService.setIsLoggedIn(false); // Set logged-in status to false on error
          return []; // Return an empty array or handle the error as needed
        }),

        tap((authenticated: boolean) => {
          if (authenticated) {
            this.tokenService.setIsLoggedIn(authenticated);
          }
        }),
        finalize(() => {})
      )
      .subscribe();
  }
}
