import { OrderModel } from './OrderModel';
import { ProductModel } from './ProductModel';

export class OrderItemModel {
  order?: OrderModel;
  product?: ProductModel;
  quantity?: number;
  price?: number;
}
