import { Component, effect, inject, signal } from '@angular/core';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ProductForm } from '../../components/product-form/product-form';

@Component({
  selector: 'app-product',
  imports: [MatTableModule, MatButtonModule],
  templateUrl: './product.html',
  styleUrl: './product.css',
})
export class Product {
  private dialog = inject(MatDialog);

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
    this.productService.findAll().subscribe((res) => this.products.set(res.content));
  }
}
