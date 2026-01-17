package com.ignite.mastery.controller;

import com.ignite.mastery.model.Product;
import com.ignite.mastery.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Product Controller
 * 
 * Purpose: Provides REST APIs for the Product Management System.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Endpoints for managing products in Apache Ignite")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Stores a product in the Ignite cache with indexing")
    public Product createProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products from the Ignite cluster")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a specific product from the Ignite cluster")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get cache metrics", description = "Provides real-time hit/miss statistics for the Ignite cache")
    public Map<String, Object> getCacheMetrics() {
        return productService.getCacheMetrics();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Removes a product from the Ignite cache")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}
