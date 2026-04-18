import { Component, effect, inject, signal } from '@angular/core';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ProductForm } from '../../components/product-form/product-form';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-product',
  imports: [MatTableModule, MatButtonModule, MatPaginatorModule],
  templateUrl: './product.html',
  styleUrl: './product.css',
})
export class Product {
  private dialog = inject(MatDialog);
  pageSize = signal(10);
  pageIndex = signal(0);
  totalElements = signal(0);

  columns: String[] = ['id', 'name', 'price', 'quantity', 'imgUrl', 'select'];

  product = new ProductModel();

  products = signal<ProductModel[]>([]);

  dataSource = new MatTableDataSource<ProductModel>();

  constructor(
    private productService: ProductService,
    private route: Router,
  ) {
    effect(() => {
      this.dataSource.data = this.products();
    });
  }

  ngOnInit(): void {
    this.findAll();
  }

  openDialog() {
    this.dialog.open(ProductForm, {
      width: '50vw',
    });
  }

  findAll(): void {
    this.productService.findAll(this.pageIndex(), this.pageSize()).subscribe((res) => {
      console.log('res:', res);
      this.products.set(res.content);
      this.totalElements.set(res.totalElements);
    });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.findAll();
  }
}
