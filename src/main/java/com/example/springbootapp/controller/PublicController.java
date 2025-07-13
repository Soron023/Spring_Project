package com.example.springbootapp.controller;

import com.example.springbootapp.dto.CategoryDto;
import com.example.springbootapp.dto.ProductDto;
import com.example.springbootapp.entity.Category;
import com.example.springbootapp.entity.Product;
import com.example.springbootapp.service.CategoryService;
import com.example.springbootapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    // Public Product Endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllActiveProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products/page")
    public ResponseEntity<Page<Product>> getAllActiveProductsPaginated(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getActiveProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Product>> getActiveProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchActiveProducts(@RequestParam String keyword, Pageable pageable) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products/price-range")
    public ResponseEntity<Page<Product>> getActiveProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        Page<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products/available")
    public ResponseEntity<Page<Product>> getAvailableProducts(Pageable pageable) {
        Page<Product> products = productService.getAvailableProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    // Public Category Endpoints
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllActiveCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getActiveCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping("/categories/with-count")
    public ResponseEntity<List<CategoryDto>> getAllActiveCategoriesWithProductCount() {
        List<CategoryDto> categories = categoryService.getAllCategoriesWithProductCount();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/search")
    public ResponseEntity<List<Category>> searchActiveCategories(@RequestParam String keyword) {
        List<Category> categories = categoryService.searchCategoriesByName(keyword);
        return ResponseEntity.ok(categories);
    }
    
    // Health Check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Public API is running");
    }
} 