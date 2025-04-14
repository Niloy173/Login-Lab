export class User {
  userid?: number | undefined;
  username?: string | undefined;
  email?: string | undefined;
  password?: string | undefined;
  role?: string | undefined;

  constructor(user: any) {
    this.userid = user.userid;
    this.username = user.username;
    this.email = user.email;
    this.password = user.password;
    this.role = user.role;
  }
}
