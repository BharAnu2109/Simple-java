import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from './restaurant';

export interface OrderItem {
  menuItemId: number;
  quantity: number;
  specialInstructions?: string;
  customizations?: string;
}

export interface CreateOrderRequest {
  customerId: number;
  restaurantId: number;
  orderType: 'DINE_IN' | 'TAKEAWAY' | 'DELIVERY';
  deliveryAddress?: string;
  specialInstructions?: string;
  items: OrderItem[];
  paymentMethod?: string;
}

export interface OrderResponse {
  id: number;
  customerId: number;
  restaurantId: number;
  status: string;
  totalAmount: number;
  taxAmount: number;
  discountAmount: number;
  orderType: string;
  deliveryAddress: string;
  specialInstructions: string;
  estimatedDeliveryTime: string;
  actualDeliveryTime: string;
  paymentMethod: string;
  paymentStatus: string;
  createdAt: string;
  updatedAt: string;
  items: OrderItemResponse[];
}

export interface OrderItemResponse {
  id: number;
  menuItemId: number;
  menuItemName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  specialInstructions: string;
  customizations: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8082/api/orders';

  constructor(private http: HttpClient) { }

  createOrder(order: CreateOrderRequest): Observable<ApiResponse<OrderResponse>> {
    return this.http.post<ApiResponse<OrderResponse>>(this.apiUrl, order);
  }

  getOrderById(orderId: number): Observable<ApiResponse<OrderResponse>> {
    return this.http.get<ApiResponse<OrderResponse>>(`${this.apiUrl}/${orderId}`);
  }

  getCustomerOrders(customerId: number): Observable<ApiResponse<OrderResponse[]>> {
    return this.http.get<ApiResponse<OrderResponse[]>>(`${this.apiUrl}/customer/${customerId}`);
  }

  getCustomerOrder(customerId: number, orderId: number): Observable<ApiResponse<OrderResponse>> {
    return this.http.get<ApiResponse<OrderResponse>>(`${this.apiUrl}/customer/${customerId}/order/${orderId}`);
  }

  getRestaurantOrders(restaurantId: number): Observable<ApiResponse<OrderResponse[]>> {
    return this.http.get<ApiResponse<OrderResponse[]>>(`${this.apiUrl}/restaurant/${restaurantId}`);
  }

  updateOrderStatus(orderId: number, status: string, reason?: string): Observable<ApiResponse<OrderResponse>> {
    let params: any = { status };
    if (reason) {
      params.reason = reason;
    }
    return this.http.put<ApiResponse<OrderResponse>>(`${this.apiUrl}/${orderId}/status`, null, { params });
  }

  cancelOrder(customerId: number, orderId: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/customer/${customerId}/order/${orderId}/cancel`, null);
  }
}
