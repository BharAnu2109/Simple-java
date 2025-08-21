import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Restaurant {
  id: number;
  name: string;
  description: string;
  address: string;
  phoneNumber: string;
  email: string;
  cuisineType: string;
  openingTime: string;
  closingTime: string;
  isActive: boolean;
  deliveryEnabled: boolean;
  takeawayEnabled: boolean;
  dineInEnabled: boolean;
  deliveryRadiusKm: number;
  minimumOrderAmount: number;
  deliveryFee: number;
  latitude: number;
  longitude: number;
  imageUrl: string;
  rating: number;
  totalReviews: number;
}

export interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  imageUrl: string;
  available: boolean;
  preparationTimeMinutes: number;
  allergenInfo: string;
  nutritionalInfo: string;
  restaurantId: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
  path: string;
}

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private apiUrl = 'http://localhost:8081/api/restaurants';

  constructor(private http: HttpClient) { }

  getAllRestaurants(): Observable<ApiResponse<Restaurant[]>> {
    return this.http.get<ApiResponse<Restaurant[]>>(this.apiUrl);
  }

  getRestaurantById(id: number): Observable<ApiResponse<Restaurant>> {
    return this.http.get<ApiResponse<Restaurant>>(`${this.apiUrl}/${id}`);
  }

  searchRestaurants(params: any): Observable<ApiResponse<Restaurant[]>> {
    return this.http.get<ApiResponse<Restaurant[]>>(`${this.apiUrl}/search`, { params });
  }

  getMenuItems(restaurantId: number, category?: string): Observable<ApiResponse<MenuItem[]>> {
    let params: any = {};
    if (category) {
      params.category = category;
    }
    return this.http.get<ApiResponse<MenuItem[]>>(`${this.apiUrl}/${restaurantId}/menu`, { params });
  }

  getMenuCategories(restaurantId: number): Observable<ApiResponse<string[]>> {
    return this.http.get<ApiResponse<string[]>>(`${this.apiUrl}/${restaurantId}/menu/categories`);
  }

  searchMenuItems(restaurantId: number, search: string): Observable<ApiResponse<MenuItem[]>> {
    return this.http.get<ApiResponse<MenuItem[]>>(`${this.apiUrl}/${restaurantId}/menu`, { 
      params: { search } 
    });
  }
}
