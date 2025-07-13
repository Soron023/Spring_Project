package com.example.springbootapp.service.impl;

import com.example.springbootapp.dto.ProductDto;
import com.example.springbootapp.entity.Category;
import com.example.springbootapp.entity.Product;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.ProductRepository;
import com.example.springbootapp.service.ProductService;
import com.example.springbootapp.service.CategoryService;
import com.example.springbootapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue(Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    @Override
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable);
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
        User currentUser = getCurrentUser();

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(category);
        product.setCreatedBy(currentUser);
        product.setActive(true);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) {
        Product product = getProductById(id);
        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(category);
        product.setActive(productDto.isActive());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void activateProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(true);
        productRepository.save(product);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    @Override
    public Page<Product> getAvailableProducts(Pageable pageable) {
        return productRepository.findAvailableProducts(pageable);
    }

    @Override
    public Page<Product> getOutOfStockProducts(Pageable pageable) {
        return productRepository.findOutOfStockProducts(pageable);
    }

    @Override
    public List<ProductDto> getAllProductsWithCategoryName() {
        List<Product> products = productRepository.findByActiveTrue(Pageable.unpaged()).getContent();

        return products.stream().map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStockQuantity(product.getStockQuantity());
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
            dto.setActive(product.isActive());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductDtoById(Long id) {
        Product product = getProductById(id);
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        dto.setActive(product.isActive());
        return dto;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }
} 