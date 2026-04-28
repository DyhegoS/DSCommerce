import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoriesModel } from '../models/CategoriesModel';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private url: string = 'http://localhost:8080/categories';

  constructor(private http: HttpClient) {}

  findAll(): Observable<CategoriesModel[]> {
    return this.http.get<CategoriesModel[]>(this.url);
  }
}
