import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { OrdersService } from '../../core/services/orders.service';
import { Order } from '../../shared/models/order.model';

@Component({
  standalone: true,
  imports: [CurrencyPipe, DatePipe],
  template: `
    <section class="page-heading"><div><span class="eyebrow">GESTIÓN</span><h1>Pedidos</h1><p>Información obtenida a través del BFF autenticado.</p></div>
      <div class="actions"><button (click)="createDemo()">Crear pedido demo</button><button (click)="load()">Actualizar</button></div></section>
    @if (loading()) { <div class="state">Cargando pedidos…</div> }
    @else if (error()) { <div class="state error">{{ error() }}</div> }
    @else {
      <div class="table-wrap"><table><thead><tr><th>ID</th><th>Cliente</th><th>Estado</th><th>Fecha</th><th>Total</th></tr></thead>
      <tbody>@for (order of orders(); track order.id) {<tr><td>#{{ order.id }}</td><td>{{ order.customerName }}</td>
      <td><span class="status">{{ order.status }}</span></td><td>{{ order.createdAt | date:'dd-MM-yyyy HH:mm' }}</td>
      <td>{{ order.total | currency:'CLP':'symbol-narrow':'1.0-0' }}</td></tr>}</tbody></table></div>
    }
  `
})
export class OrdersComponent implements OnInit {
  readonly orders = signal<Order[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  constructor(private readonly service: OrdersService) {}
  ngOnInit(): void { this.load(); }
  createDemo(): void {
    this.loading.set(true); this.error.set('');
    this.service.createDemo().subscribe({
      next: () => this.load(),
      error: () => { this.error.set('No fue posible crear el pedido demo.'); this.loading.set(false); }
    });
  }
  load(): void {
    this.loading.set(true); this.error.set('');
    this.service.list().subscribe({
      next: data => { this.orders.set(data); this.loading.set(false); },
      error: response => { this.error.set(response.status === 403 ? 'No tienes el rol requerido.' : 'No fue posible consultar el BFF.'); this.loading.set(false); }
    });
  }
}
