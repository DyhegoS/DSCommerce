import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Product } from '../_models/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private products: Product[] = [
    {
      id: 1,
      name: 'Notebook Dell XPS 13',
      description: 'Laptop ultraportátil com processador Intel i7',
      price: 5499.99,
      quantity: 5,
      imageUrl: 'https://via.placeholder.com/200?text=Dell+XPS+13',
    },
    {
      id: 2,
      name: 'Mouse Logitech MX Master',
      description: 'Mouse ergonômico profissional com precisão avançada',
      price: 349.99,
      quantity: 15,
      imageUrl: 'https://via.placeholder.com/200?text=Logitech+Mouse',
    },
    {
      id: 3,
      name: 'Teclado Mecânico Razer',
      description: 'Teclado RGB com switches mecânicos Cherry',
      price: 599.99,
      quantity: 8,
      imageUrl: 'https://via.placeholder.com/200?text=Razer+Keyboard',
    },
    {
      id: 4,
      name: 'Monitor LG UltraWide 34"',
      description: 'Monitor ultrawide 3440x1440 para produtividade',
      price: 2899.99,
      quantity: 3,
      imageUrl: 'https://via.placeholder.com/200?text=LG+Monitor',
    },
    {
      id: 5,
      name: 'Webcam Logitech 4K',
      description: 'Câmera 4K com autofoco e correção de luz',
      price: 799.99,
      quantity: 12,
      imageUrl: 'https://via.placeholder.com/200?text=Logitech+Webcam',
    },
  ];

  private productsSubject = new BehaviorSubject<Product[]>(this.products);
  products$ = this.productsSubject.asObservable();

  constructor() {}

  getProducts(): Observable<Product[]> {
    return this.products$;
  }

  getProductById(id: number): Product | undefined {
    return this.products.find((p) => p.id === id);
  }

  addProduct(product: Product): void {
    const newProduct = { ...product, id: this.getNextId() };
    this.products.push(newProduct);
    this.productsSubject.next([...this.products]);
  }

  updateProduct(id: number, product: Partial<Product>): void {
    const index = this.products.findIndex((p) => p.id === id);
    if (index !== -1) {
      this.products[index] = { ...this.products[index], ...product };
      this.productsSubject.next([...this.products]);
    }
  }

  deleteProduct(id: number): void {
    this.products = this.products.filter((p) => p.id !== id);
    this.productsSubject.next([...this.products]);
  }

  private getNextId(): number {
    return Math.max(...this.products.map((p) => p.id), 0) + 1;
  }
}
