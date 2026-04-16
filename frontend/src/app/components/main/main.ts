import { Component } from '@angular/core';
import { Product } from '../../pages/product/product';

@Component({
  selector: 'app-main',
  imports: [Product],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {}
