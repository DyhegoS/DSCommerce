
import { Routes } from '@angular/router';
import { ProductsComponent } from './_components/products/products';
import { OrdersComponent } from './_components/orders/orders';
import { Home } from './_components/home/home';
import { UsersComponent } from './_components/users/users';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'products', component: ProductsComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'home', component: Home },
  { path: 'users', component: UsersComponent}
];
