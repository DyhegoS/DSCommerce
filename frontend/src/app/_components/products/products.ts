import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../_services/product.service';
import { Product } from '../../_models/product';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-products',
  imports: [CommonModule],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  selectedImageUrl: string | null = null;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
    });
  }

  openImage(imageUrl: string): void {
    this.selectedImageUrl = imageUrl;
  }

  closeImage(): void {
    this.selectedImageUrl = null;
  }

  editProduct(product: Product): void {
    console.log('Editar produto:', product);
    // Implementar lógica de edição
  }

  deleteProduct(id: number): void {
    if (confirm('Tem certeza que deseja deletar este produto?')) {
      this.productService.deleteProduct(id);
    }
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  }
}
