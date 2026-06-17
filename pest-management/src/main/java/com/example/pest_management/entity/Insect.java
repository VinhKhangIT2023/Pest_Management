package com.example.pest_management.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "insects")
public class Insect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String scientificName;

    @Column(columnDefinition = "TEXT")
    private String damage;

    @Column(columnDefinition = "TEXT")
    private String solution;
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String prevention;

    @OneToMany(mappedBy = "insect", cascade = CascadeType.ALL)
    private List<Product> products;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrevention() { return prevention; }
    public void setPrevention(String prevention) { this.prevention = prevention; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDamage() { return damage; }
    public void setDamage(String damage) { this.damage = damage; }

    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
}