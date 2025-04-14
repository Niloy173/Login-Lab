import { Component, SkipSelf } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, finalize, tap } from 'rxjs';
import { TokenService } from 'src/app/shared/services/storage/token.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  credentials: any = {
    email: '',
    password: '',
  };

  constructor(
    @SkipSelf() private tokenService: TokenService,
    private router: Router,
    @SkipSelf() private authService: AuthService
  ) {}

  submitForm(e: Event, form: NgForm) {
    e.preventDefault();
    console.log(this.credentials);

    this.authService
      .login(this.credentials)
      .pipe(
        tap((res) => {
          console.log(res);
        }),
        finalize(() => {
          this.resetForm(form);
        }),
        catchError((err) => {
          console.log(err);
          return [];
        })
      )
      .subscribe();
  }

  checkValue(e: Event): void {
    //console.log(this.credentials);
    this.isFormValid();
  }

  isFormValid(): boolean {
    return Object.keys(this.credentials).every(
      (k) => this.credentials[k] !== ''
    );
  }

  resetForm(form: NgForm) {
    form.reset({
      email: '',
      password: '',
    });
  }
}
