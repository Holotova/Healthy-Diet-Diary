package com.project.demoSpringDiary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Meal {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Double weight;
    private Double caloriesPerServing;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DiaryUser user;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private Day day;


    public Meal(Product product, Double weight, LocalDate date) {
        this.product = product;
        this.weight = weight;
        this.date = date;

    }
}

