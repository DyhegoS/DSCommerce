import { Component, inject, signal, OnInit } from '@angular/core';
import { OrderModel } from '../../models/OrderModel';
import { OrderService } from '../../services/order-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-order',
  imports: [MatTableModule, MatButtonModule, MatPaginatorModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order implements OnInit {
  private orderService = inject(OrderService);

  columns: string[] = ['id', 'moment', 'status', 'user', 'client', 'paymentStatus'];

  orders = signal<OrderModel[]>([]);
  dataSource = new MatTableDataSource<OrderModel>();
  pageSize = signal(10);
  pageIndex = signal(0);
  totalElements = signal(0);

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.findAll(this.pageIndex(), this.pageSize()).subscribe({
      next: (data) => {
        this.orders.set(data.content);
        this.dataSource.data = data.content;
        this.totalElements.set(data.totalElements);
      },
      error: (err) => console.error('Error loading orders', err),
    });
  }

  onPageChange(event: any) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadOrders();
  }
}
