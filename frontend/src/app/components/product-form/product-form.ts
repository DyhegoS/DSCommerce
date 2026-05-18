import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { CategoriesModel } from '../../models/CategoriesModel';
import { ProductModel } from '../../models/ProductModel';

interface ProductFormData {
  product?: ProductModel;
  categories?: CategoriesModel[];
}

@Component({
  selector: 'app-product-form',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './product-form.html',
  styleUrls: ['./product-form.css'],
})
export class ProductForm {
  btnInsert: boolean = true;
  private data = inject(MAT_DIALOG_DATA) as ProductFormData;

  categories: CategoriesModel[] = this.data?.categories ?? [];

  productForm = new FormGroup({
    id: new FormControl<number | null>(null),
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    description: new FormControl('', [Validators.required, Validators.minLength(10)]),
    price: new FormControl<number | null>(null, [Validators.required, Validators.min(0.01)]),
    quantity: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    imgUrl: new FormControl(''),
    categories: new FormControl<number[]>([], [Validators.required]),
  });

  constructor(private dialogRef: MatDialogRef<ProductForm>) {
    if (this.data?.product) {
      this.btnInsert = false;
      const product = this.data.product;
      this.productForm.patchValue({
        id: product.id ?? null,
        name: product.name ?? '',
        description: product.description ?? '',
        price: product.price ?? null,
        quantity: product.quantity ?? null,
        imgUrl: product.imgUrl ?? '',
        categories: product.categories?.map((category) => category.id!) ?? [],
      });
    }
  }

  onSubmit() {
    const formValue = this.productForm.value as {
      id: number | null;
      name: string;
      description: string;
      price: number | null;
      quantity: number | null;
      imgUrl: string;
      categories: number[] | null;
    };

    const product: ProductModel = {
      id: formValue.id ?? undefined,
      name: formValue.name,
      description: formValue.description,
      price: formValue.price ?? undefined,
      quantity: formValue.quantity ?? undefined,
      imgUrl: formValue.imgUrl,
      categories: (formValue.categories ?? []).map((categoryId) => ({ id: categoryId })),
    };
    this.dialogRef.close(product);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
