import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ClientModel } from '../../models/ClientModel';

@Component({
  selector: 'app-client-form',
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './client-form.html',
  styleUrls: ['./client-form.css'],
})
export class ClientForm {
  btnInsert: boolean = true;
  data = inject(MAT_DIALOG_DATA) as ClientModel;

  clientForm = new FormGroup({
    id: new FormControl(),
    name: new FormControl(),
    email: new FormControl(),
    cnpj: new FormControl(),
    address: new FormControl(),
    phone: new FormControl(),
  });

  constructor(private dialogRef: MatDialogRef<ClientForm>) {
    if (this.data) {
      this.btnInsert = false;
      this.clientForm.patchValue(this.data);
    }
  }

  onSubmit() {
    this.dialogRef.close(this.clientForm.value);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
