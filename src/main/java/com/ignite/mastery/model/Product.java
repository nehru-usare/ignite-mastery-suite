package com.ignite.mastery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Product Model
 * 
 * Purpose: This class represents a product in our system.
 * 
 * Note: It implements Serializable because Apache Ignite needs to
 * transfer this object across the network to different nodes.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    // We use a long ID as the primary key
    @Id
    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField(index = true)
    private String name;
    private String description;
    @QuerySqlField(index = true)
    private double price;
    @QuerySqlField
    private int quantity;
}
