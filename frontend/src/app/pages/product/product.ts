import { Component, effect, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-product',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './product.html',
  styleUrl: './product.css',
})
export class Product {
  btnInsert: boolean = true;

  columns: String[] = ['id', 'name', 'description', 'price', 'quantity', 'imgUrl'];

  product = new ProductModel();

  products = signal<ProductModel[]>([]);

  dataSource = new MatTableDataSource<ProductModel>();

  productForm = new FormGroup({
    id: new FormControl(),
    name: new FormControl(),
    description: new FormControl(),
    price: new FormControl(),
    quantity: new FormControl(),
    imgUrl: new FormControl(),
  });

  constructor(private productService: ProductService) {
    effect(() => {
      this.dataSource.data = this.products();
    });
  }

  ngOnInit(): void {
    this.findAll();
  }

  findAll(): void {
    this.productService.findAll().subscribe((res) => this.products.set(res.content));
  }
}
