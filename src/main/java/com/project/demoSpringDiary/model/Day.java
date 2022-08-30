package com.project.demoSpringDiary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Day {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFromDiary;
    private Double dailyCalorieGoal;
    private Double dailyCalorieConsumption;
    private Double dailyCalorieBalance;

    @OneToOne
    private DiaryUser user;

    @OneToMany(mappedBy = "day", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Meal> dailyMeals;

    public Day(LocalDate dateFromDiary, DiaryUser user) {
        this.dateFromDiary = dateFromDiary;
        this.user = user;
    }
}
