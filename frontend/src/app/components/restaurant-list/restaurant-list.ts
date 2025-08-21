import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RestaurantService, Restaurant } from '../../services/restaurant';
import { Router } from '@angular/router';

@Component({
  selector: 'app-restaurant-list',
  imports: [CommonModule],
  templateUrl: './restaurant-list.html',
  styleUrl: './restaurant-list.scss'
})
export class RestaurantListComponent implements OnInit {
  restaurants: Restaurant[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private restaurantService: RestaurantService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadRestaurants();
  }

  loadRestaurants() {
    this.loading = true;
    this.error = null;
    
    this.restaurantService.getAllRestaurants().subscribe({
      next: (response) => {
        if (response.success) {
          this.restaurants = response.data;
        } else {
          this.error = response.message;
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load restaurants';
        this.loading = false;
        console.error('Error loading restaurants:', err);
      }
    });
  }

  viewMenu(restaurantId: number) {
    this.router.navigate(['/menu', restaurantId]);
  }

  searchRestaurants(searchTerm: string) {
    if (!searchTerm.trim()) {
      this.loadRestaurants();
      return;
    }

    this.loading = true;
    this.restaurantService.searchRestaurants({ name: searchTerm }).subscribe({
      next: (response) => {
        if (response.success) {
          this.restaurants = response.data;
        } else {
          this.error = response.message;
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Search failed';
        this.loading = false;
        console.error('Search error:', err);
      }
    });
  }
}
