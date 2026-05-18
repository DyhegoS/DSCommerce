import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientModel } from '../../models/ClientModel';
import { ClientService } from '../../services/client-service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ClientForm } from '../../components/client-form/client-form';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-client',
  imports: [
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatPaginatorModule,
    MatIconModule,
  ],
  templateUrl: './client.html',
  styleUrl: './client.css',
})
export class ClientPage implements OnInit {
  private dialog = inject(MatDialog);
  private clientService = inject(ClientService);

  columns: string[] = ['id', 'name', 'email', 'cnpj', 'address', 'phone'];

  clients = signal<ClientModel[]>([]);
  dataSource = new MatTableDataSource<ClientModel>();
  nameFilter = signal('');
  cnpjFilter = signal('');
  pageSize = signal(10);
  pageIndex = signal(0);
  totalElements = signal(0);

  constructor() {
    this.dataSource = new MatTableDataSource<ClientModel>();
  }

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.clientService
      .findAll(this.nameFilter(), this.cnpjFilter(), this.pageIndex(), this.pageSize())
      .subscribe({
        next: (res) => {
          this.clients.set(res.content);
          this.dataSource.data = res.content;
          this.totalElements.set(res.totalElements);
        },
        error: (err) => console.error('Error loading clients', err),
      });
  }

  openForm() {
    const dialogRef = this.dialog.open(ClientForm, {
      width: '40vw',
    });

    dialogRef.afterClosed().subscribe((result: ClientModel | undefined) => {
      if (result) {
        this.clientService.insert(result).subscribe({
          next: () => {
            this.loadClients();
            alert('Cliente cadastrado com sucesso!');
          },
          error: (err) => {
            console.error('Error inserting client', err);
            alert('Falha ao cadastrar cliente.');
          },
        });
      }
    });
  }

  search() {
    this.pageIndex.set(0);
    this.loadClients();
  }

  clearFilters() {
    this.nameFilter.set('');
    this.cnpjFilter.set('');
    this.pageIndex.set(0);
    this.loadClients();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadClients();
  }
}
