import { Component, SkipSelf } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, finalize, tap } from 'rxjs';
import { User } from '../../model/User';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  userInfo: User = {
    username: '',
    email: '',
    password: '',
    role: 'user',
  };

  constructor(
    @SkipSelf() private authService: AuthService,
    private router: Router
  ) {}

  submitForm(e: Event, form: NgForm) {
    e.preventDefault();

    const registerObject = new User(this.userInfo);
    console.log(registerObject);

    this.authService
      .register(registerObject)
      .pipe(
        tap((res) => {
          console.log(res);
        }),
        finalize(() => {
          form.reset({
            username: '',
            email: '',
            password: '',
            role: '',
          });
        }),
        catchError((err) => {
          console.log(err);
          return [];
        })
      )
      .subscribe();
  }

  checkValue(e: Event): void {
    // console.log(this.userInfo);
    this.isFormValid();
  }

  isFormValid(): boolean {
    return (Object.keys(this.userInfo) as (keyof User)[]).every(
      (k) => this.userInfo[k] !== ''
    );
  }
}
