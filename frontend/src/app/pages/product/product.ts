import { CategoryService } from './../../services/category-service';
import { Component, effect, inject, Inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductForm } from '../../components/product-form/product-form';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { CategoriesModel } from '../../models/CategoriesModel';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-product',
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatDialogModule,
    MatPaginatorModule,
    ScrollingModule,
    MatTabsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
  ],
  templateUrl: './product.html',
  styleUrls: ['./product.css'],
})
export class Product implements OnInit {
  private dialog = inject(MatDialog);
  pageSize = signal(10);
  pageIndex = signal(0);
  totalElements = signal(0);

  columns: string[] = ['id', 'name', 'categories', 'price', 'quantity', 'imgUrl', 'actions'];

  products = signal<ProductModel[]>([]);
  categories: CategoriesModel[] = [];
  selectedCategory = signal('');
  selectedName = signal('');
  minPrice = signal<number | null>(null);
  maxPrice = signal<number | null>(null);

  dataSource = new MatTableDataSource<ProductModel>();

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
  ) {
    effect(() => {
      this.dataSource.data = this.products();
    });
  }

  ngOnInit(): void {
    this.findAll();
    this.findAllCategories();
  }

  openDialog(product?: ProductModel) {
    const dialogRef = this.dialog.open(ProductForm, {
      width: '50vw',
      data: {
        product,
        categories: this.categories,
      },
    });

    dialogRef.afterClosed().subscribe((result: ProductModel | undefined) => {
      if (result) {
        this.saveProduct(result);
      }
    });
  }

  findAllCategories(): void {
    this.categoryService.findAll().subscribe((res) => (this.categories = res));
  }

  findAll(): void {
    this.productService
      .findAll(this.pageIndex(), this.pageSize(), this.selectedName(), this.selectedCategory())
      .subscribe((res) => {
        this.products.set(res.content);
        this.totalElements.set(res.totalElements);
      });
  }

  onPriceFilter(): void {
    const min = this.minPrice();
    const max = this.maxPrice();

    if (min === null || max === null) return;
    if (min > max) {
      alert('Preço mínimo não pode ser maior que o máximo!');
      return;
    }

    this.pageIndex.set(0);
    this.productService
      .findByPrice(min, max, this.pageIndex(), this.pageSize())
      .subscribe((res) => {
        this.products.set(res.content);
        this.totalElements.set(res.totalElements);
      });
  }

  onPriceClear(): void {
    this.minPrice.set(null);
    this.maxPrice.set(null);
    this.findAll();
  }

  onCategoryChange(categoryName: string): void {
    this.selectedCategory.set(categoryName);
    this.pageIndex.set(0);
    this.findAll();
  }

  onNameChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.selectedName.set(value);
    this.pageIndex.set(0);
    this.findAll();
  }

  saveProduct(product: ProductModel): void {
    if (product.id) {
      this.productService.update(product.id, product).subscribe({
        next: () => {
          this.findAll();
          alert('Produto atualizado com sucesso!');
        },
        error: (err) => {
          console.error('Error updating product', err);
          alert('Falha ao atualizar o produto.');
        },
      });
      return;
    }

    this.productService.insert(product).subscribe({
      next: () => {
        this.findAll();
        alert('Produto cadastrado com sucesso!');
      },
      error: (err) => {
        console.error('Error inserting product', err);
        alert('Falha ao cadastrar o produto.');
      },
    });
  }

  deleteProduct(id: number) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) {
      return;
    }

    this.productService.delete(id).subscribe({
      next: () => this.findAll(),
      error: (err) => {
        console.error('Error deleting product', err);
        alert('Falha ao excluir o produto.');
      },
    });
  }

  showImage(product: ProductModel) {
    if (!product.imgUrl) {
      return;
    }

    this.dialog.open(ProductImageDialog, {
      width: '45vw',
      data: product,
    });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.findAll();
  }
}

@Component({
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>{{ data.name }}</h2>
    <mat-dialog-content class="product-image-dialog-content">
      <img [src]="data.imgUrl" [alt]="data.name" class="product-image-dialog" />
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-flat-button color="primary" mat-dialog-close>Fechar</button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      .product-image-dialog-content {
        display: flex;
        justify-content: center;
        padding: 16px;
      }
      .product-image-dialog {
        max-width: 100%;
        max-height: 70vh;
        object-fit: contain;
      }
    `,
  ],
})
export class ProductImageDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ProductModel) {}
}
