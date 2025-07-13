package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    
    List<Category> findByActiveTrue();
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT c FROM Category c WHERE c.active = true AND " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> searchByName(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.name ASC")
    List<Category> findAllActiveOrderByName();
} 