import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserModel } from '../models/UserModel';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private url: string = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  findAll(page: number, size: number): Observable<PageResponse<UserModel>> {
    return this.http.get<PageResponse<UserModel>>(`${this.url}?page=${page}&size=${size}`);
  }

  findById(id: number): Observable<UserModel> {
    return this.http.get<UserModel>(`${this.url}/${id}`);
  }

  insert(obj: UserModel): Observable<UserModel> {
    return this.http.post<UserModel>(this.url, obj);
  }

  update(id: number, obj: UserModel): Observable<UserModel> {
    return this.http.put<UserModel>(`${this.url}/${id}`, obj);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  findMe(): Observable<UserModel> {
    return this.http.get<UserModel>(`${this.url}/me`);
  }
}
