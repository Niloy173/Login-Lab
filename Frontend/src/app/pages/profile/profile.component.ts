import { Component, OnInit, SkipSelf } from '@angular/core';
import { catchError, tap } from 'rxjs';
import { AuthService } from 'src/app/auth/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  constructor(@SkipSelf() private authService: AuthService) {}

  ngOnInit(): void {
    // Initialization logic here
    this.authService
      .profile()
      .pipe(
        tap((res) => console.log(res)),
        catchError((err) => {
          console.log(err);
          return [];
        })
      )
      .subscribe();
  }

  // Add any additional methods or properties as needed
}
