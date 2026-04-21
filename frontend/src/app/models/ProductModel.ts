import { CategoriesModel } from './CategoriesModel';

export class ProductModel {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  quantity?: number;
  imgUrl?: string;
  // categories?: CategoriesModel[];
}
