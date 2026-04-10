import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductModel } from '../models/ProductModel';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private url: string = 'http://localhost:8080/products';

  constructor(private http: HttpClient) {}

  findAll(page = 0, size = 10): Observable<PageResponse<ProductModel>> {
    return this.http.get<PageResponse<ProductModel>>(`${this.url}?oage=${page}&size${size}`);
  }
}
