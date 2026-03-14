import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../_services/order.service';
import { Order } from '../../_models/order';

@Component({
  selector: 'app-orders',
  imports: [CommonModule],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  selectedOrder: Order | null = null;
  showOrderDetails = false;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getOrders().subscribe((orders) => {
      this.orders = orders;
    });
  }

  openOrderDetails(order: Order): void {
    this.selectedOrder = order;
    this.showOrderDetails = true;
  }

  closeOrderDetails(): void {
    this.showOrderDetails = false;
    this.selectedOrder = null;
  }

  viewOrderItems(order: Order): void {
    this.selectedOrder = order;
    this.showOrderDetails = true;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Separação':
        return 'status-separacao';
      case 'Enviado':
        return 'status-enviado';
      case 'Cancelado':
        return 'status-cancelado';
      default:
        return '';
    }
  }

  getPaymentStatusClass(status: string): string {
    switch (status) {
      case 'Pago':
        return 'payment-paid';
      case 'Pendente':
        return 'payment-pending';
      case 'Cancelado':
        return 'payment-canceled';
      default:
        return '';
    }
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('pt-BR');
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  }
}
