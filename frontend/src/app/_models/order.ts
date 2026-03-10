export type OrderStatus = 'Separação' | 'Enviado' | 'Cancelado';
export type PaymentStatus = 'Pago' | 'Pendente' | 'Cancelado';

export interface OrderItem {
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

export interface Order {
  id: number;
  date: Date;
  status: OrderStatus;
  user: string;
  paymentStatus: PaymentStatus;
  items: OrderItem[];
  totalAmount: number;
}
