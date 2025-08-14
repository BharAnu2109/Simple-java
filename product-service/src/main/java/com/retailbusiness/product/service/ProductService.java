package com.retailbusiness.product.service;

import com.retailbusiness.product.dto.ProductDto;
import com.retailbusiness.product.entity.Product;
import com.retailbusiness.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return convertToDto(product);
    }

    public ProductDto getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + sku));
        return convertToDto(product);
    }

    public ProductDto createProduct(ProductDto productDto) {
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + productDto.getSku() + " already exists");
        }
        
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Check if SKU is being changed and if it's already taken
        if (!existingProduct.getSku().equals(productDto.getSku()) && 
            productRepository.existsBySku(productDto.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + productDto.getSku() + " already exists");
        }

        updateProductFields(existingProduct, productDto);
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        product.setIsActive(false);
        product.setStatus(Product.ProductStatus.DISCONTINUED);
        productRepository.save(product);
    }

    public List<ProductDto> getActiveProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> searchProducts(String query) {
        return productRepository.findByNameOrDescriptionContaining(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return productRepository.findAllActiveCategories();
    }

    public List<String> getAllBrands() {
        return productRepository.findAllActiveBrands();
    }

    public boolean updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        
        product.setStockQuantity(quantity);
        if (quantity <= 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        } else if (quantity <= product.getMinStockLevel()) {
            product.setStatus(Product.ProductStatus.AVAILABLE);
        } else {
            product.setStatus(Product.ProductStatus.AVAILABLE);
        }
        
        productRepository.save(product);
        return true;
    }

    public boolean reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        
        if (product.getStockQuantity() < quantity) {
            return false; // Insufficient stock
        }
        
        product.setStockQuantity(product.getStockQuantity() - quantity);
        if (product.getStockQuantity() <= 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        }
        
        productRepository.save(product);
        return true;
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setSku(product.getSku());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setMinStockLevel(product.getMinStockLevel());
        dto.setIsActive(product.getIsActive());
        dto.setStatus(product.getStatus());
        dto.setWeightKg(product.getWeightKg());
        dto.setDimensions(product.getDimensions());
        return dto;
    }

    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        updateProductFields(product, dto);
        return product;
    }

    private void updateProductFields(Product product, ProductDto dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSku(dto.getSku());
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 0);
        product.setMinStockLevel(dto.getMinStockLevel() != null ? dto.getMinStockLevel() : 0);
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : Product.ProductStatus.AVAILABLE);
        product.setWeightKg(dto.getWeightKg());
        product.setDimensions(dto.getDimensions());
    }
}