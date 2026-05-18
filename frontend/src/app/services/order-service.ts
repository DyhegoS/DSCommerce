import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderModel } from '../models/OrderModel';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private url: string = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  findAll(page: number, size: number): Observable<PageResponse<OrderModel>> {
    return this.http.get<PageResponse<OrderModel>>(`${this.url}?page=${page}&size=${size}`);
  }

  findById(id: number): Observable<OrderModel> {
    return this.http.get<OrderModel>(`${this.url}/${id}`);
  }

  insert(obj: OrderModel): Observable<OrderModel> {
    return this.http.post<OrderModel>(this.url, obj);
  }
}
