import { Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Product } from './pages/product/product';

@Component({
  selector: 'app-root',
  imports: [RouterModule, Product],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
