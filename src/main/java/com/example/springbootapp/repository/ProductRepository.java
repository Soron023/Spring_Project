package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    Page<Product> findByActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND p.active = true")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.active = true")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 AND p.active = true")
    Page<Product> findAvailableProducts(Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 AND p.active = true")
    Page<Product> findOutOfStockProducts(Pageable pageable);
    
    long countByCategoryIdAndActiveTrue(Long categoryId);
} 