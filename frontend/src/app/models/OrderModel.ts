import { UserModel } from './UserModel';
import { ClientModel } from './ClientModel';
import { OrderItemModel } from './OrderItemModel';

export class OrderModel {
  id?: number;
  moment?: string;
  updateMoment?: string;
  status?: string;
  user?: UserModel;
  userUpdate?: UserModel;
  client?: ClientModel;
  items?: OrderItemModel[];
}
