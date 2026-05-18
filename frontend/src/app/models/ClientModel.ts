import { UserModel } from './UserModel';

export class ClientModel {
  id?: number;
  name?: string;
  email?: string;
  cnpj?: string;
  address?: string;
  phone?: string;
  user?: UserModel;
}
