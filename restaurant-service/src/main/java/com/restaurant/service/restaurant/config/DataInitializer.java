package com.restaurant.service.restaurant.config;

import com.restaurant.common.entity.MenuItem;
import com.restaurant.service.restaurant.entity.Restaurant;
import com.restaurant.service.restaurant.repository.MenuItemRepository;
import com.restaurant.service.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase(RestaurantRepository restaurantRepository,
                                 MenuItemRepository menuItemRepository) {
        return args -> {
            if (restaurantRepository.count() == 0) {
                log.info("Initializing sample data...");
                
                // Create sample restaurants
                Restaurant pizzaPalace = Restaurant.builder()
                        .name("Pizza Palace")
                        .description("Authentic Italian pizzas with fresh ingredients")
                        .address("123 Main St, Downtown")
                        .phoneNumber("+1234567890")
                        .email("info@pizzapalace.com")
                        .cuisineType(Restaurant.CuisineType.ITALIAN)
                        .openingTime(LocalTime.of(11, 0))
                        .closingTime(LocalTime.of(23, 0))
                        .isActive(true)
                        .deliveryEnabled(true)
                        .takeawayEnabled(true)
                        .dineInEnabled(true)
                        .deliveryRadiusKm(10.0)
                        .minimumOrderAmount(15.0)
                        .deliveryFee(3.99)
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .imageUrl("https://images.unsplash.com/photo-1513104890138-7c749659a591")
                        .rating(4.5)
                        .totalReviews(128L)
                        .build();
                
                Restaurant burgerBarn = Restaurant.builder()
                        .name("Burger Barn")
                        .description("Gourmet burgers and craft beverages")
                        .address("456 Oak Ave, Midtown")
                        .phoneNumber("+1234567891")
                        .email("hello@burgerbarn.com")
                        .cuisineType(Restaurant.CuisineType.AMERICAN)
                        .openingTime(LocalTime.of(10, 0))
                        .closingTime(LocalTime.of(22, 0))
                        .isActive(true)
                        .deliveryEnabled(true)
                        .takeawayEnabled(true)
                        .dineInEnabled(true)
                        .deliveryRadiusKm(8.0)
                        .minimumOrderAmount(12.0)
                        .deliveryFee(2.99)
                        .latitude(40.7589)
                        .longitude(-73.9851)
                        .imageUrl("https://images.unsplash.com/photo-1571091718767-18b5b1457add")
                        .rating(4.2)
                        .totalReviews(89L)
                        .build();
                
                Restaurant spiceGarden = Restaurant.builder()
                        .name("Spice Garden")
                        .description("Authentic Indian cuisine with traditional spices")
                        .address("789 Curry Lane, Little India")
                        .phoneNumber("+1234567892")
                        .email("orders@spicegarden.com")
                        .cuisineType(Restaurant.CuisineType.INDIAN)
                        .openingTime(LocalTime.of(12, 0))
                        .closingTime(LocalTime.of(23, 30))
                        .isActive(true)
                        .deliveryEnabled(true)
                        .takeawayEnabled(true)
                        .dineInEnabled(true)
                        .deliveryRadiusKm(12.0)
                        .minimumOrderAmount(20.0)
                        .deliveryFee(4.99)
                        .latitude(40.7505)
                        .longitude(-73.9934)
                        .imageUrl("https://images.unsplash.com/photo-1565557623262-b51c2513a641")
                        .rating(4.7)
                        .totalReviews(203L)
                        .build();
                
                restaurantRepository.save(pizzaPalace);
                restaurantRepository.save(burgerBarn);
                restaurantRepository.save(spiceGarden);
                
                // Create sample menu items for Pizza Palace
                createPizzaMenu(menuItemRepository, pizzaPalace.getId());
                
                // Create sample menu items for Burger Barn
                createBurgerMenu(menuItemRepository, burgerBarn.getId());
                
                // Create sample menu items for Spice Garden
                createIndianMenu(menuItemRepository, spiceGarden.getId());
                
                log.info("Sample data initialization completed!");
            }
        };
    }
    
    private void createPizzaMenu(MenuItemRepository repository, Long restaurantId) {
        repository.save(MenuItem.builder()
                .name("Margherita Pizza")
                .description("Classic pizza with tomato sauce, mozzarella, and fresh basil")
                .price(BigDecimal.valueOf(16.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1604068549290-dea0e4a305ca")
                .available(true)
                .preparationTimeMinutes(15)
                .allergenInfo("Contains gluten, dairy")
                .nutritionalInfo("520 cal, 22g protein, 45g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Pepperoni Pizza")
                .description("Traditional pizza with pepperoni and mozzarella cheese")
                .price(BigDecimal.valueOf(18.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1628840042765-356cda07504e")
                .available(true)
                .preparationTimeMinutes(18)
                .allergenInfo("Contains gluten, dairy")
                .nutritionalInfo("680 cal, 28g protein, 48g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Caesar Salad")
                .description("Fresh romaine lettuce with Caesar dressing and croutons")
                .price(BigDecimal.valueOf(12.99))
                .category(MenuItem.MenuCategory.SALAD)
                .imageUrl("https://images.unsplash.com/photo-1551248429-40975aa4de74")
                .available(true)
                .preparationTimeMinutes(10)
                .allergenInfo("Contains gluten, dairy, eggs")
                .nutritionalInfo("320 cal, 8g protein, 15g carbs")
                .restaurantId(restaurantId)
                .build());
    }
    
    private void createBurgerMenu(MenuItemRepository repository, Long restaurantId) {
        repository.save(MenuItem.builder()
                .name("Classic Cheeseburger")
                .description("Beef patty with cheese, lettuce, tomato, and special sauce")
                .price(BigDecimal.valueOf(14.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd")
                .available(true)
                .preparationTimeMinutes(12)
                .allergenInfo("Contains gluten, dairy")
                .nutritionalInfo("650 cal, 35g protein, 42g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Bacon BBQ Burger")
                .description("Double beef patty with bacon, BBQ sauce, and onion rings")
                .price(BigDecimal.valueOf(17.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1553979459-d2229ba7433a")
                .available(true)
                .preparationTimeMinutes(15)
                .allergenInfo("Contains gluten, dairy")
                .nutritionalInfo("850 cal, 45g protein, 48g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Sweet Potato Fries")
                .description("Crispy sweet potato fries with sea salt")
                .price(BigDecimal.valueOf(6.99))
                .category(MenuItem.MenuCategory.SIDE_DISH)
                .imageUrl("https://images.unsplash.com/photo-1573080496219-bb080dd4f877")
                .available(true)
                .preparationTimeMinutes(8)
                .allergenInfo("Vegan friendly")
                .nutritionalInfo("280 cal, 3g protein, 52g carbs")
                .restaurantId(restaurantId)
                .build());
    }
    
    private void createIndianMenu(MenuItemRepository repository, Long restaurantId) {
        repository.save(MenuItem.builder()
                .name("Chicken Tikka Masala")
                .description("Tender chicken in creamy tomato-based curry sauce")
                .price(BigDecimal.valueOf(19.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1565557623262-b51c2513a641")
                .available(true)
                .preparationTimeMinutes(20)
                .allergenInfo("Contains dairy")
                .nutritionalInfo("420 cal, 32g protein, 18g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Vegetable Biryani")
                .description("Fragrant basmati rice with mixed vegetables and aromatic spices")
                .price(BigDecimal.valueOf(16.99))
                .category(MenuItem.MenuCategory.MAIN_COURSE)
                .imageUrl("https://images.unsplash.com/photo-1563379091339-03246963d96c")
                .available(true)
                .preparationTimeMinutes(25)
                .allergenInfo("Vegan friendly")
                .nutritionalInfo("380 cal, 12g protein, 68g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Garlic Naan")
                .description("Traditional Indian bread with garlic and herbs")
                .price(BigDecimal.valueOf(4.99))
                .category(MenuItem.MenuCategory.SIDE_DISH)
                .imageUrl("https://images.unsplash.com/photo-1601050690597-df0568f70950")
                .available(true)
                .preparationTimeMinutes(10)
                .allergenInfo("Contains gluten, dairy")
                .nutritionalInfo("180 cal, 6g protein, 32g carbs")
                .restaurantId(restaurantId)
                .build());
        
        repository.save(MenuItem.builder()
                .name("Mango Lassi")
                .description("Traditional yogurt drink with sweet mango flavor")
                .price(BigDecimal.valueOf(5.99))
                .category(MenuItem.MenuCategory.BEVERAGE)
                .imageUrl("https://images.unsplash.com/photo-1570197788417-0e82375c9371")
                .available(true)
                .preparationTimeMinutes(5)
                .allergenInfo("Contains dairy")
                .nutritionalInfo("150 cal, 4g protein, 28g carbs")
                .restaurantId(restaurantId)
                .build());
    }
}