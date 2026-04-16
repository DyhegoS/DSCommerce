import { Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Product } from './pages/product/product';
import { Main } from './components/main/main';
import { Header } from './components/header/header';

@Component({
  selector: 'app-root',
  imports: [RouterModule, Header, Main],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
