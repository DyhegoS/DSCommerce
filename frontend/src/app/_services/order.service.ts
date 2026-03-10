import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Order } from '../_models/order';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private orders: Order[] = [
    {
      id: 1001,
      date: new Date('2026-03-08'),
      status: 'Separação',
      user: 'João Silva',
      paymentStatus: 'Pago',
      items: [
        { productId: 1, productName: 'Notebook Dell XPS 13', quantity: 1, price: 5499.99 },
        { productId: 2, productName: 'Mouse Logitech MX Master', quantity: 2, price: 349.99 },
      ],
      totalAmount: 6199.97,
    },
    {
      id: 1002,
      date: new Date('2026-03-07'),
      status: 'Enviado',
      user: 'Maria Santos',
      paymentStatus: 'Pago',
      items: [
        { productId: 3, productName: 'Teclado Mecânico Razer', quantity: 1, price: 599.99 },
      ],
      totalAmount: 599.99,
    },
    {
      id: 1003,
      date: new Date('2026-03-06'),
      status: 'Enviado',
      user: 'Pedro Oliveira',
      paymentStatus: 'Pendente',
      items: [
        { productId: 4, productName: 'Monitor LG UltraWide 34"', quantity: 1, price: 2899.99 },
      ],
      totalAmount: 2899.99,
    },
    {
      id: 1004,
      date: new Date('2026-03-05'),
      status: 'Cancelado',
      user: 'Ana Costa',
      paymentStatus: 'Cancelado',
      items: [
        { productId: 5, productName: 'Webcam Logitech 4K', quantity: 1, price: 799.99 },
      ],
      totalAmount: 799.99,
    },
    {
      id: 1005,
      date: new Date('2026-03-04'),
      status: 'Separação',
      user: 'Carlos Mendes',
      paymentStatus: 'Pago',
      items: [
        { productId: 1, productName: 'Notebook Dell XPS 13', quantity: 1, price: 5499.99 },
        { productId: 3, productName: 'Teclado Mecânico Razer', quantity: 1, price: 599.99 },
        { productId: 5, productName: 'Webcam Logitech 4K', quantity: 1, price: 799.99 },
      ],
      totalAmount: 6899.97,
    },
  ];

  private ordersSubject = new BehaviorSubject<Order[]>(this.orders);
  orders$ = this.ordersSubject.asObservable();

  constructor() {}

  getOrders(): Observable<Order[]> {
    return this.orders$;
  }

  getOrderById(id: number): Order | undefined {
    return this.orders.find((o) => o.id === id);
  }

  updateOrder(id: number, order: Partial<Order>): void {
    const index = this.orders.findIndex((o) => o.id === id);
    if (index !== -1) {
      this.orders[index] = { ...this.orders[index], ...order };
      this.ordersSubject.next([...this.orders]);
    }
  }

  deleteOrder(id: number): void {
    this.orders = this.orders.filter((o) => o.id !== id);
    this.ordersSubject.next([...this.orders]);
  }
}
