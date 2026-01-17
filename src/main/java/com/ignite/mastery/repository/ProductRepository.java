package com.ignite.mastery.repository;

import com.ignite.mastery.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Product Repository
 * 
 * Purpose: This interface handles standard database operations using Spring Data JPA.
 * It connects to our H2 Database (simulating a real production database).
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
