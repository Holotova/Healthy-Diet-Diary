package com.project.demoSpringDiary.repository;

import com.project.demoSpringDiary.model.Category;
import com.project.demoSpringDiary.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    List<Product> findByCategory(Category category);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findAllByOrderByProductNameAsc();
    List<Product> findAllByOrderByProductNameDesc();
    List<Product> findAllByOrderByCaloriesPer100gAsc();
    List<Product> findAllByOrderByCaloriesPer100gDesc();
}
