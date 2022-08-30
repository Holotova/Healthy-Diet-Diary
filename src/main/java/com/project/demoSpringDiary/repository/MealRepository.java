package com.project.demoSpringDiary.repository;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.model.Meal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends CrudRepository<Meal, String> {
    List<Meal> findByDateAndUser (LocalDate date, DiaryUser user);
    List<Meal> findByUser (DiaryUser user);


}
