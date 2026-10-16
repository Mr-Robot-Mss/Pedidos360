import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Order } from '../../shared/models/order.model';

@Injectable({ providedIn: 'root' })
export class OrdersService {
  constructor(private readonly http: HttpClient) {}
  list(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/orders`);
  }

  createDemo(): Observable<Order> {
    return this.http.post<Order>(`${environment.apiUrl}/orders`, {
      customerName: `Cliente Demo ${new Date().toLocaleTimeString('es-CL')}`,
      status: 'PENDIENTE',
      total: 129990
    });
  }
}
