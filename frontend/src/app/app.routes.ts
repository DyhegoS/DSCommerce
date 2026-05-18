import { Routes } from '@angular/router';
import { Product } from './pages/product/product';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Category } from './pages/category/category';
import { User } from './pages/user/user';
import { ClientPage } from './pages/client/client';
import { Order } from './pages/order/order';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', component: Home, canActivate: [authGuard] },
  { path: 'products', component: Product, canActivate: [authGuard] },
  { path: 'categories', component: Category, canActivate: [authGuard] },
  { path: 'users', component: User, canActivate: [authGuard] },
  { path: 'clients', component: ClientPage, canActivate: [authGuard] },
  { path: 'orders', component: Order, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
