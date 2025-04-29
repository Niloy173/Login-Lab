import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  SkipSelf,
} from '@angular/core';
import {
  catchError,
  filter,
  finalize,
  Observable,
  ReplaySubject,
  Subject,
  takeUntil,
  tap,
} from 'rxjs';
import { User } from 'src/app/auth/model/User';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss'],
})
export class UsersComponent implements OnInit, AfterViewInit, OnDestroy {
  userSubject$: ReplaySubject<User[]> = new ReplaySubject<User[]>(1);
  data$: Observable<User[]> = this.userSubject$.asObservable();

  allUsers: User[] = [];
  users: User[] = []; // Array to hold the users that are loaded
  modalLoading: boolean = false;
  errorData: any = null;

  destroy$: Subject<void> = new Subject();

  // Initial position and batch size
  currentIndex = 0;
  batchSize = 15;

  constructor(@SkipSelf() private userService: UserService) {}

  ngOnInit(): void {
    this.allUsers = [];
    this.modalLoading = true;

    this.userService
      .getAllUsers()
      .pipe(
        takeUntil(this.destroy$),
        catchError((err) => {
          this.errorData = err?.error || null;
          return [];
        }),
        tap((res: any) => {
          const data = res?.data || [];

          this.allUsers = [...this.allUsers, ...data];
          const firstTimeFormate = data.slice(
            this.currentIndex,
            this.currentIndex + this.batchSize
          );
          this.userSubject$.next(
            firstTimeFormate.map((user: any) => new User(user))
          );
        }),
        finalize(() => {
          this.modalLoading = false;
        })
      )
      .subscribe();

    this.data$
      .pipe(
        filter<User[]>(Boolean),
        tap((users: User[]) => {
          if (users.length > 0) {
            this.users = users;

            console.log('All users : ', this.users);
            console.log('Current number of users : ', this.users.length);
          }
        }),
        finalize(() => {})
      )
      .subscribe();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
    this.destroy$.next(); // emits new value
    this.destroy$.complete(); // successfully unsubscribing to those which has bindings for avoding memroy leaks
  }

  // Add any additional methods or properties as needed
}
