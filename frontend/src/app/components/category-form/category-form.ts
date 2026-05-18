import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoriesModel } from '../../models/CategoriesModel';

@Component({
  selector: 'app-category-form',
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './category-form.html',
  styleUrl: './category-form.css',
})
export class CategoryForm {
  btnInsert: boolean = true;
  data = inject(MAT_DIALOG_DATA);

  categoryForm = new FormGroup({
    id: new FormControl(),
    name: new FormControl(),
  });

  constructor(private dialogRef: MatDialogRef<CategoryForm>) {
    if (this.data) {
      this.btnInsert = false;
      this.categoryForm.patchValue(this.data);
    }
  }

  onSubmit() {
    this.dialogRef.close(this.categoryForm.value);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
