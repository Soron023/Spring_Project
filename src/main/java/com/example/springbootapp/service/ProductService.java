package com.example.springbootapp.service;

import com.example.springbootapp.dto.ProductDto;
import com.example.springbootapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
    List<Product> getProductsByCategory(Long categoryId);
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);
    Product createProduct(ProductDto productDto);
    Product updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    void deactivateProduct(Long id);
    void activateProduct(Long id);
    Page<Product> searchProducts(String keyword, Pageable pageable);
    Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> getAvailableProducts(Pageable pageable);
    Page<Product> getOutOfStockProducts(Pageable pageable);
    List<ProductDto> getAllProductsWithCategoryName();
    ProductDto getProductDtoById(Long id);
} 