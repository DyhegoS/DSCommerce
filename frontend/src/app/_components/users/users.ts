import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../_services/users.service';
import { Users } from '../../_models/users';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  imports: [CommonModule],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class UsersComponent implements OnInit {
  users: Users[] = [];
  selectedUser: Users | null = null;

  constructor(private usersService: UsersService) {}

  ngOnInit(): void {
      this.usersService.getUsers().subscribe((users) => {
        this.users = users;
      });
  }

  editUser(user: Users): void {
      console.log('Editar usuário:', user);
      // Implementar lógica de edição
    }

  deleteUser(id: number): void {
    if (confirm('Tem certeza que deseja deletar este usuário?')) {
      this.usersService.deleteUser(id);
    }
  }

  getRole(role: string): string {
    if(role === 'admin') {
      return 'badge-admin';
    }else if(role ==='user') {
      return 'badge-user';
    }
    return '';
  }
}
