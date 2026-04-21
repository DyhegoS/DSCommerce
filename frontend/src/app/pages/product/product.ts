import { Component, effect, inject, signal } from '@angular/core';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ProductForm } from '../../components/product-form/product-form';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ScrollingModule } from '@angular/cdk/scrolling';

@Component({
  selector: 'app-product',
  imports: [MatTableModule, MatButtonModule, MatPaginatorModule, ScrollingModule],
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

  constructor(private productService: ProductService) {
    effect(() => {
      this.dataSource.data = this.products();
    });
  }

  ngOnInit(): void {
    this.findAll();
  }

  openDialog() {
    const dialogRef = this.dialog.open(ProductForm, {
      width: '50vw',
    });

    dialogRef.afterClosed().subscribe((data: ProductModel) => {
      if (data) {
        this.insert(data);
      }
    });
  }

  findAll(): void {
    this.productService.findAll(this.pageIndex(), this.pageSize()).subscribe((res) => {
      this.products.set(res.content);
      this.totalElements.set(res.totalElements);
    });
  }

  insert(product: ProductModel): void {
    this.productService.insert(product).subscribe((res) => {
      this.products.update((currProducts) => [...currProducts, res]);

      this.product = new ProductModel();

      alert('Produto cadastrado com sucesso!');
    });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.findAll();
  }
}
