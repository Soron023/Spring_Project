package com.example.springbootapp.controller;

import com.example.springbootapp.dto.CategoryDto;
import com.example.springbootapp.entity.Category;
import com.example.springbootapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/ordered")
    public ResponseEntity<List<Category>> getAllCategoriesOrderByName() {
        List<Category> categories = categoryService.getAllCategoriesOrderByName();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/with-count")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesWithProductCount() {
        List<CategoryDto> categories = categoryService.getAllCategoriesWithProductCount();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Map<String, String>> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deactivated successfully");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Map<String, String>> activateCategory(@PathVariable Long id) {
        categoryService.activateCategory(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category activated successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String keyword) {
        List<Category> categories = categoryService.searchCategoriesByName(keyword);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/check/name/{name}")
    public ResponseEntity<Map<String, Boolean>> checkCategoryNameExists(@PathVariable String name) {
        boolean exists = categoryService.existsByName(name);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
} 