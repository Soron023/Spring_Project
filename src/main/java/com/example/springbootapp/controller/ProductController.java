package com.example.springbootapp.controller;

import com.example.springbootapp.dto.ProductDto;
import com.example.springbootapp.entity.Product;
import com.example.springbootapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getAllProductsPaginated(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/dto")
    public ResponseEntity<List<ProductDto>> getAllProductsWithCategoryName() {
        List<ProductDto> products = productService.getAllProductsWithCategoryName();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/{id}/dto")
    public ResponseEntity<ProductDto> getProductDtoById(@PathVariable Long id) {
        ProductDto product = productService.getProductDtoById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{categoryId}/page")
    public ResponseEntity<Page<Product>> getProductsByCategoryPaginated(@PathVariable Long categoryId, Pageable pageable) {
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        Product product = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(product);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Map<String, String>> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deactivated successfully");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Map<String, String>> activateProduct(@PathVariable Long id) {
        productService.activateProduct(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product activated successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam String keyword, Pageable pageable) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<Page<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        Page<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/available")
    public ResponseEntity<Page<Product>> getAvailableProducts(Pageable pageable) {
        Page<Product> products = productService.getAvailableProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<Product>> getOutOfStockProducts(Pageable pageable) {
        Page<Product> products = productService.getOutOfStockProducts(pageable);
        return ResponseEntity.ok(products);
    }
} 