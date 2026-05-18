import { Component, inject, signal, OnInit } from '@angular/core';
import { UserModel } from '../../models/UserModel';
import { UserService } from '../../services/user-service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { UserForm } from '../../components/user-form/user-form';

@Component({
  selector: 'app-user',
  imports: [MatTableModule, MatButtonModule, MatPaginatorModule, MatDialogModule, MatIconModule],
  templateUrl: './user.html',
  styleUrl: './user.css',
})
export class User implements OnInit {
  private userService = inject(UserService);
  private dialog = inject(MatDialog);

  columns: string[] = ['id', 'name', 'username', 'email', 'actions'];

  users = signal<UserModel[]>([]);
  dataSource = new MatTableDataSource<UserModel>();
  pageSize = signal(10);
  pageIndex = signal(0);
  totalElements = signal(0);

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.findAll(this.pageIndex(), this.pageSize()).subscribe({
      next: (data) => {
        this.users.set(data.content);
        this.dataSource.data = data.content;
        this.totalElements.set(data.totalElements);
      },
      error: (err) => console.error('Error loading users', err),
    });
  }

  openForm(user?: UserModel) {
    const dialogRef = this.dialog.open(UserForm, {
      width: '40vw',
      data: user,
    });

    dialogRef.afterClosed().subscribe((result: UserModel | undefined) => {
      if (!result) {
        return;
      }

      if (user?.id) {
        this.userService.update(user.id, result).subscribe({
          next: () => {
            this.loadUsers();
            alert('Usuário atualizado com sucesso!');
          },
          error: (err) => {
            console.error('Error updating user', err);
            alert('Falha ao atualizar o usuário.');
          },
        });
      } else {
        this.userService.insert(result).subscribe({
          next: () => {
            this.loadUsers();
            alert('Usuário cadastrado com sucesso!');
          },
          error: (err) => {
            console.error('Error inserting user', err);
            alert('Falha ao cadastrar usuário.');
          },
        });
      }
    });
  }

  deleteUser(id: number) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.userService.delete(id).subscribe({
        next: () => this.loadUsers(),
        error: (err) => console.error('Error deleting user', err),
      });
    }
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadUsers();
  }
}
