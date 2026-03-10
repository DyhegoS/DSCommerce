
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Users } from '../_models/users';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private users: Users[] = [
    { id: 1, name: 'João Silva', email: 'joao.silva@example.com', role: 'user' },
    { id: 2, name: 'admin', email: 'admin@example.com', role: 'admin' },
  ];

  private ordersSubject = new BehaviorSubject<Users[]>(this.users);
  users$ = this.ordersSubject.asObservable();

  constructor() {}

  getUsers(): Observable<Users[]> {
      return this.users$;
  }

  deleteUser(id: number): void {
    this.users = this.users.filter((u) => u.id !== id);
    this.ordersSubject.next([...this.users]);
  }
}
