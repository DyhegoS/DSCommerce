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

  // findAll(page: number, size: number): Observable<PageResponse<ProductModel>> {
  //   return this.http.get<PageResponse<ProductModel>>(`${this.url}?page=${page}&size=${size}`);
  // }

  findAll(
    page: number,
    size: number,
    name: string = '',
    categoryName: string = '',
  ): Observable<PageResponse<ProductModel>> {
    return this.http.get<PageResponse<ProductModel>>(
      `${this.url}?page=${page}&size=${size}&name=${name}&categoryName=${categoryName}`,
    );
  }

  findByPrice(
    min: number,
    max: number,
    page: number,
    size: number,
  ): Observable<PageResponse<ProductModel>> {
    return this.http.get<PageResponse<ProductModel>>(
      `${this.url}/price?min=${min}&max=${max}&page=${page}&size=${size}`,
    );
  }

  insert(obj: ProductModel): Observable<ProductModel> {
    return this.http.post<ProductModel>(this.url, obj);
  }
}
