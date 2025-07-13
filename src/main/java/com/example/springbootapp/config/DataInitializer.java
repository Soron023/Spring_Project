package com.example.springbootapp.config;

import com.example.springbootapp.entity.Category;
import com.example.springbootapp.entity.Product;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.repository.CategoryRepository;
import com.example.springbootapp.repository.ProductRepository;
import com.example.springbootapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.initialize-data", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize users if none exist
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        
        // Initialize categories if none exist
        if (categoryRepository.count() == 0) {
            initializeCategories();
        }
        
        // Initialize products if none exist
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }
    
    private void initializeUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);
        
        // Create moderator user
        User moderator = new User();
        moderator.setUsername("moderator");
        moderator.setEmail("moderator@example.com");
        moderator.setPassword(passwordEncoder.encode("mod123"));
        moderator.setFirstName("Moderator");
        moderator.setLastName("User");
        moderator.setRole("MODERATOR");
        moderator.setEnabled(true);
        userRepository.save(moderator);
        
        // Create regular user
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("Regular");
        user.setLastName("User");
        user.setRole("USER");
        user.setEnabled(true);
        userRepository.save(user);
        
        System.out.println("Users initialized successfully!");
    }
    
    private void initializeCategories() {
        List<Category> categories = Arrays.asList(
            new Category("Electronics", "Electronic devices and gadgets"),
            new Category("Clothing", "Fashion and apparel"),
            new Category("Books", "Books and publications"),
            new Category("Home & Garden", "Home improvement and garden supplies"),
            new Category("Sports", "Sports equipment and accessories"),
            new Category("Toys", "Toys and games"),
            new Category("Automotive", "Automotive parts and accessories"),
            new Category("Health & Beauty", "Health and beauty products")
        );
        
        categoryRepository.saveAll(categories);
        System.out.println("Categories initialized successfully!");
    }
    
    private void initializeProducts() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) return;
        
        List<Category> categories = categoryRepository.findAll();
        
        // Electronics products
        Category electronics = categories.stream()
            .filter(c -> c.getName().equals("Electronics"))
            .findFirst().orElse(null);
        
        if (electronics != null) {
            List<Product> electronicsProducts = Arrays.asList(
                new Product("iPhone 15", "Latest iPhone with advanced features", new BigDecimal("999.99"), electronics),
                new Product("Samsung Galaxy S24", "Premium Android smartphone", new BigDecimal("899.99"), electronics),
                new Product("MacBook Pro", "Professional laptop for developers", new BigDecimal("1999.99"), electronics),
                new Product("Sony WH-1000XM5", "Premium noise-canceling headphones", new BigDecimal("349.99"), electronics)
            );
            
            electronicsProducts.forEach(product -> {
                product.setCreatedBy(admin);
                product.setStockQuantity(50);
            });
            
            productRepository.saveAll(electronicsProducts);
        }
        
        // Clothing products
        Category clothing = categories.stream()
            .filter(c -> c.getName().equals("Clothing"))
            .findFirst().orElse(null);
        
        if (clothing != null) {
            List<Product> clothingProducts = Arrays.asList(
                new Product("Nike Air Max", "Comfortable running shoes", new BigDecimal("129.99"), clothing),
                new Product("Levi's 501 Jeans", "Classic blue jeans", new BigDecimal("79.99"), clothing),
                new Product("Adidas T-Shirt", "Comfortable cotton t-shirt", new BigDecimal("29.99"), clothing),
                new Product("North Face Jacket", "Warm winter jacket", new BigDecimal("199.99"), clothing)
            );
            
            clothingProducts.forEach(product -> {
                product.setCreatedBy(admin);
                product.setStockQuantity(100);
            });
            
            productRepository.saveAll(clothingProducts);
        }
        
        // Books products
        Category books = categories.stream()
            .filter(c -> c.getName().equals("Books"))
            .findFirst().orElse(null);
        
        if (books != null) {
            List<Product> bookProducts = Arrays.asList(
                new Product("The Great Gatsby", "Classic American novel by F. Scott Fitzgerald", new BigDecimal("12.99"), books),
                new Product("1984", "Dystopian novel by George Orwell", new BigDecimal("11.99"), books),
                new Product("To Kill a Mockingbird", "Harper Lee's masterpiece", new BigDecimal("13.99"), books),
                new Product("The Hobbit", "Fantasy novel by J.R.R. Tolkien", new BigDecimal("14.99"), books)
            );
            
            bookProducts.forEach(product -> {
                product.setCreatedBy(admin);
                product.setStockQuantity(200);
            });
            
            productRepository.saveAll(bookProducts);
        }
        
        System.out.println("Products initialized successfully!");
    }
} 