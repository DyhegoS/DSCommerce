import { CategoryService } from './../../services/category-service';
import { Component, effect, inject, signal } from '@angular/core';
import { ProductModel } from '../../models/ProductModel';
import { ProductService } from '../../services/product-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ProductForm } from '../../components/product-form/product-form';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { CategoriesModel } from '../../models/CategoriesModel';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-product',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    ScrollingModule,
    MatTabsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
  ],
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
    this.findAll(); // volta ao findAll sem filtro
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
