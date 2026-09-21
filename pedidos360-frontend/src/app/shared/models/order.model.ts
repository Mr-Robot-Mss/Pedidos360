export interface Order {
  id: number;
  customerName: string;
  status: 'PENDIENTE' | 'EN_PREPARACION' | 'DESPACHADO';
  total: number;
  createdAt: string;
}
