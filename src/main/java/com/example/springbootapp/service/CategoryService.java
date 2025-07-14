package com.example.springbootapp.service;

import com.example.springbootapp.dto.CategoryDto;
import com.example.springbootapp.entity.Category;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.CategoryRepository;
import com.example.springbootapp.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Category> getAllCategories() {
        return categoryRepository.findByActiveTrue();
    }
    
    public List<Category> getAllCategoriesOrderByName() {
        return categoryRepository.findAllActiveOrderByName();
    }
    
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
    
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }
    
    public Category createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
        }
        
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setActive(true);
        
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(Long id, CategoryDto categoryDto) {
        Category category = getCategoryById(id);
        
        // Check if name is being changed and if it already exists
        if (!category.getName().equals(categoryDto.getName()) && 
            categoryRepository.existsByName(categoryDto.getName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
        }
        
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setActive(categoryDto.isActive());
        
        return categoryRepository.save(category);
    }
    
    public Category updateCategoryWithBeanUtils(Long id, Category updatedData) {
        Category category = getCategoryById(id);
        BeanUtils.copyProperties(updatedData, category, getNullPropertyNames(updatedData));
        // Do not update id
        return categoryRepository.save(category);
    }

    private String[] getNullPropertyNames(Object source) {
        try {
            java.beans.BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(source.getClass(), Object.class);
            return java.util.Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(pd -> {
                        try {
                            return pd.getReadMethod().invoke(source) == null;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .map(java.beans.PropertyDescriptor::getName)
                    .toArray(String[]::new);
        } catch (java.beans.IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        
        // Check if category has products
        long productCount = productRepository.countByCategoryIdAndActiveTrue(id);
        if (productCount > 0) {
            throw new RuntimeException("Cannot delete category with " + productCount + " active products");
        }
        
        categoryRepository.delete(category);
    }
    
    public void deactivateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }
    
    public void activateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(true);
        categoryRepository.save(category);
    }
    
    public List<Category> searchCategoriesByName(String keyword) {
        return categoryRepository.searchByName(keyword);
    }
    
    public List<CategoryDto> getAllCategoriesWithProductCount() {
        List<Category> categories = categoryRepository.findByActiveTrue();
        
        return categories.stream().map(category -> {
            CategoryDto dto = new CategoryDto();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());
            dto.setActive(category.isActive());
            dto.setProductCount((int) productRepository.countByCategoryIdAndActiveTrue(category.getId()));
            return dto;
        }).collect(Collectors.toList());
    }
    
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
} 