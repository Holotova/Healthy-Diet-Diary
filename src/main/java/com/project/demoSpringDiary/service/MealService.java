package com.project.demoSpringDiary.service;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.model.Meal;
import com.project.demoSpringDiary.model.Product;
import com.project.demoSpringDiary.repository.MealRepository;
import com.project.demoSpringDiary.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    MealRepository repository;
    ProductRepository productRepository;

    @Autowired
    public MealService(MealRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public Double getCaloriesPerServing(Meal meal) {
        Double caloriesPerServing = (meal.getProduct().getCaloriesPer100g() / 100) * meal.getWeight();
        return caloriesPerServing;
    }

    public Iterable<Meal> getAllMeals(){
        return repository.findAll();
    }

    public Meal getMealById(String id) {
        Optional<Meal> optional = repository.findById(id);
        Meal meal;
        if (optional.isPresent()) {
            meal = optional.get();
        } else {
            throw new RuntimeException(" Meal not found for id : " + id);
        }
        return meal;
    }

    public boolean isSaveDailyMeal(String productName, Double weight, LocalDate date) {
        DiaryUser user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Meal> dailyMeal = new ArrayList<>();
        Product product = null;
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        if (products.size() == 1) {
            product = products.get(0);
            Meal meal = new Meal(product, weight, date);
            meal.setUser(user);
            meal.setCaloriesPerServing(getCaloriesPerServing(meal));
            repository.save(meal);
            dailyMeal.add(meal);
            return true;
        } else {
            return false;
        }
    }

    public Iterable<Meal> getMealsByDateCurrentUser(LocalDate date) {
        Iterable<Meal> meals;
        DiaryUser user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (date != null && !date.toString().isEmpty()) {
            meals = repository.findByDateAndUser(date, user);
        } else {
            meals = repository.findByDateAndUser(LocalDate.now(), user);
        }
        return meals;
    }

    public void saveEditMeal(Double weight, String id) {
        Meal meal = getMealById(id);
        meal.setWeight(weight);
        meal.setCaloriesPerServing(getCaloriesPerServing(meal));
        repository.save(meal);
    }


}
