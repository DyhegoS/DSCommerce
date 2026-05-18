import { Component, inject, signal, OnInit } from '@angular/core';
import { CategoriesModel } from '../../models/CategoriesModel';
import { CategoryService } from '../../services/category-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CategoryForm } from '../../components/category-form/category-form';

@Component({
  selector: 'app-category',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatDialogModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './category.html',
  styleUrl: './category.css',
})
export class Category implements OnInit {
  private dialog = inject(MatDialog);
  private categoryService = inject(CategoryService);

  columns: string[] = ['id', 'name', 'actions'];

  categories = signal<CategoriesModel[]>([]);
  dataSource = new MatTableDataSource<CategoriesModel>();

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.findAll().subscribe({
      next: (data) => {
        this.categories.set(data);
        this.dataSource.data = data;
      },
      error: (err) => console.error('Error loading categories', err),
    });
  }

  openForm(category?: CategoriesModel) {
    const dialogRef = this.dialog.open(CategoryForm, {
      data: category,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (category) {
          // update
          this.categoryService.update(category.id!, result).subscribe({
            next: () => this.loadCategories(),
            error: (err) => console.error('Error updating category', err),
          });
        } else {
          // insert
          this.categoryService.insert(result).subscribe({
            next: () => this.loadCategories(),
            error: (err) => console.error('Error inserting category', err),
          });
        }
      }
    });
  }

  deleteCategory(id: number) {
    if (confirm('Are you sure?')) {
      this.categoryService.delete(id).subscribe({
        next: () => this.loadCategories(),
        error: (err) => console.error('Error deleting category', err),
      });
    }
  }
}
