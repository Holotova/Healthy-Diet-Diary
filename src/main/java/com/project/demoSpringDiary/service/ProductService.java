package com.project.demoSpringDiary.service;

import com.project.demoSpringDiary.model.Category;
import com.project.demoSpringDiary.model.Meal;
import com.project.demoSpringDiary.model.Product;
import com.project.demoSpringDiary.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final MealService mealService;

    @Autowired
    public ProductService(ProductRepository repository, MealService mealService) {
        this.repository = repository;
        this.mealService = mealService;
    }

    public void createAndSaveProduct(String productName, Category category, Double calories) {
        Product product = new Product(productName, category, calories);
        repository.save(product);
    }

    public Iterable<Product> getAll() {
        return repository.findAll();
    }

    public Iterable<Product> getFilteredOrSortedProducts(String filter, String sort) {
        Iterable<Product> products;
        if (filter != null && !filter.isEmpty()) {
            products = filterProductsByCategory(filter);
        } else {
            products = sortProductsByCriteria(sort);
        }
        return products;
    }

    public Iterable<Product> filterProductsByCategory(String category) {
        Iterable<Product> products;
        if (category != null && !category.isEmpty()) {
            products = repository.findByCategory(Category.valueOf(category));
        } else {
            products = repository.findAll();
        }
        return products;
    }

    public Iterable<Product> sortProductsByCriteria(String criteria) {
        if (criteria != null && !criteria.isEmpty()) {
            return switch (criteria) {
                case "nameAsc" -> repository.findAllByOrderByProductNameAsc();
                case "nameDesc" -> repository.findAllByOrderByProductNameDesc();
                case "CaloriesAsc" -> repository.findAllByOrderByCaloriesPer100gAsc();
                case "CaloriesDesc" -> repository.findAllByOrderByCaloriesPer100gDesc();
                default -> repository.findAll();
            };
        } else {
            return repository.findAll();
        }
    }

    public boolean isProductUsedByUser(String productId) {
        List<Meal> mealList = (List<Meal>) mealService.getAllMeals();
        Optional<String> productInMeal = mealList.stream()
                .map(m -> m.getProduct())
                .map(p -> p.getId())
                .filter(id -> id.contains(productId))
                .findAny();
        return productInMeal.isPresent();
    }
}
