import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [FormsModule, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  searchQuery: string = '';
  
  user = {
    name: 'João Silva',
    role: 'Desenvolvedor',
    avatar: 'https://ui-avatars.com/api/?name=Joao+Silva&background=6366f1&color=fff'
  };

  onSearch(): void {
    if (this.searchQuery.trim()) {
      console.log('Busca por:', this.searchQuery);
      // Lógica de busca aqui
    }
  }
}
