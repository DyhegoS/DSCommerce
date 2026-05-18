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

  findById(id: number): Observable<CategoriesModel> {
    return this.http.get<CategoriesModel>(`${this.url}/${id}`);
  }

  insert(obj: CategoriesModel): Observable<CategoriesModel> {
    return this.http.post<CategoriesModel>(this.url, obj);
  }

  update(id: number, obj: CategoriesModel): Observable<CategoriesModel> {
    return this.http.put<CategoriesModel>(`${this.url}/${id}`, obj);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  findAll(): Observable<CategoriesModel[]> {
    return this.http.get<CategoriesModel[]>(this.url);
  }
}
