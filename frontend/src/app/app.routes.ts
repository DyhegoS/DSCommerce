import { Routes } from '@angular/router';
import { Product } from './pages/product/product';
import { Home } from './pages/home/home';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'products', component: Product },
];
