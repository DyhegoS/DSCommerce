import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserModel } from '../../models/UserModel';

@Component({
  selector: 'app-user-form',
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './user-form.html',
  styleUrls: ['./user-form.css'],
})
export class UserForm {
  btnInsert: boolean = true;
  data = inject(MAT_DIALOG_DATA) as UserModel;

  userForm = new FormGroup({
    id: new FormControl(),
    name: new FormControl(),
    username: new FormControl(),
    email: new FormControl(),
    password: new FormControl(),
  });

  constructor(private dialogRef: MatDialogRef<UserForm>) {
    if (this.data) {
      this.btnInsert = false;
      this.userForm.patchValue(this.data);
    }
  }

  onSubmit() {
    this.dialogRef.close(this.userForm.value);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
