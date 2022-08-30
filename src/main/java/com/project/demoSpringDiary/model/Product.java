package com.project.demoSpringDiary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String productName;

    @Enumerated(EnumType.STRING)
    private Category category;
    private Double caloriesPer100g;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Meal meal;


    public Product(String productName, Category category, Double caloriesPer100g) {
        this.productName = productName;
        this.category = category;
        this.caloriesPer100g = caloriesPer100g;
    }
}
