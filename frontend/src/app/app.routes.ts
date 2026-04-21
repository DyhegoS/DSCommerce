import { Routes } from '@angular/router';
import { Product } from './pages/product/product';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', component: Home, canActivate: [authGuard] },
  { path: 'products', component: Product, canActivate: [authGuard] },
];
