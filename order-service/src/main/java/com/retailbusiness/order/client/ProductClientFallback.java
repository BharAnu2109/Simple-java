package com.retailbusiness.order.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductClientFallback implements ProductClient {
    
    @Override
    public ProductDto getProductById(Long id) {
        // Return a fallback product to prevent order processing failure
        ProductDto fallbackProduct = new ProductDto();
        fallbackProduct.setId(id);
        fallbackProduct.setName("Product Unavailable");
        fallbackProduct.setSku("FALLBACK-SKU");
        fallbackProduct.setPrice(BigDecimal.ZERO);
        fallbackProduct.setStockQuantity(0);
        fallbackProduct.setIsActive(false);
        return fallbackProduct;
    }
    
    @Override
    public Map<String, Object> reduceStock(Long id, Map<String, Integer> request) {
        // Return failure response when product service is down
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", "Product service temporarily unavailable");
        return response;
    }
}