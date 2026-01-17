package com.ignite.mastery.service;

import com.ignite.mastery.cache.CacheNames;
import com.ignite.mastery.model.Product;
import com.ignite.mastery.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Product Service (Production Ready)
 * 
 * Purpose: This service implements a "Cache-First" strategy.
 * It uses Ignite for lightning fast reads (including SQL) and fallback to DB.
 */
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final IgniteCache<Long, Product> productCache;

    @Autowired
    public ProductService(ProductRepository productRepository, Ignite ignite) {
        this.productRepository = productRepository;
        this.productCache = ignite.getOrCreateCache(CacheNames.PRODUCT_CACHE);
    }

    /**
     * Add a product (Write-Through pattern simulation)
     */
    public Product addProduct(Product product) {
        try {
            log.info("🚀 Production Logic: Saving to DB first (Source of Truth)");
            Product savedProduct = productRepository.save(product);

            log.info("🚀 Production Logic: Updating distributed cache for HA");
            productCache.put(savedProduct.getId(), savedProduct);

            return savedProduct;
        } catch (Exception e) {
            log.error("❌ Critical error in addProduct: {}", e.getMessage());
            throw new RuntimeException("Product storage failed", e);
        }
    }

    /**
     * Get a product (Cache-Aside pattern)
     */
    public Product getProduct(Long id) {
        try {
            // Priority 1: Ignite Cache
            Product cachedProduct = productCache.get(id);
            if (cachedProduct != null) {
                log.info("✅ PROD HIT: Serving {} from Ignite RAM", id);
                return cachedProduct;
            }
        } catch (Exception e) {
            log.warn("⚠️ Ignite unreachable: Falling back to DB for lookup");
        }

        // Priority 2: Database Fallback
        return productRepository.findById(id).map(p -> {
            log.info("💾 PROD MISS: Fetching {} from DB and warming cache", id);
            try {
                productCache.putIfAbsent(p.getId(), p);
            } catch (Exception ignored) {
            }
            return p;
        }).orElse(null);
    }

    /**
     * Get all products (Distributed SQL API)
     * 
     * Note: This is significantly faster than productRepository.findAll()
     * because it uses Ignite's distributed indexing.
     */
    public List<Product> getAllProducts() {
        try {
            log.info("🔍 PROD SQL: Querying distributed cache for all items");
            List<List<?>> results = productCache.query(
                    new SqlFieldsQuery("SELECT _VAL FROM Product")).getAll();

            if (!results.isEmpty()) {
                List<Product> products = new ArrayList<>();
                for (List<?> row : results) {
                    products.add((Product) row.get(0));
                }
                log.info("✅ PROD SQL: Retrieved {} items from Ignite", products.size());
                return products;
            }
        } catch (Exception e) {
            log.warn("⚠️ Ignite SQL failed or cache empty: Getting from DB (Legacy mode)");
        }

        // Fallback to DB if cache is empty or fails
        List<Product> dbProducts = productRepository.findAll();
        log.info("💾 PROD DB: Fetched {} items from Database", dbProducts.size());

        // Async warmup
        dbProducts.forEach(p -> {
            try {
                productCache.putIfAbsent(p.getId(), p);
            } catch (Exception ignored) {
            }
        });

        return dbProducts;
    }

    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        log.info("🗑️ PROD DELETE: Removing {} from DB and Cache", id);
        try {
            productRepository.deleteById(id);
            productCache.remove(id);
        } catch (Exception e) {
            log.error("❌ Delete failed for {}: {}", id, e.getMessage());
        }
    }

    public Map<String, Object> getCacheMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        try {
            var stats = productCache.metrics();
            metrics.put("hits", stats.getCacheHits());
            metrics.put("misses", stats.getCacheMisses());
            metrics.put("hitPercentage", stats.getCacheHitPercentage());
            metrics.put("size", productCache.size());
            metrics.put("gets", stats.getCacheGets());
            metrics.put("puts", stats.getCachePuts());
            metrics.put("avgGetTime", stats.getAverageGetTime());
        } catch (Exception e) {
            metrics.put("error", "Metrics unavailable");
        }
        return metrics;
    }
}
