import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientModel } from '../models/ClientModel';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private url: string = 'http://localhost:8080/clients';

  constructor(private http: HttpClient) {}

  findAll(
    name: string,
    cnpj: string,
    page: number,
    size: number,
  ): Observable<PageResponse<ClientModel>> {
    return this.http.get<PageResponse<ClientModel>>(
      `${this.url}?name=${name}&cnpj=${cnpj}&page=${page}&size=${size}`,
    );
  }

  insert(obj: ClientModel): Observable<ClientModel> {
    return this.http.post<ClientModel>(this.url, obj);
  }
}
