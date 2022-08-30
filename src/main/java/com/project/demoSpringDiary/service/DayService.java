package com.project.demoSpringDiary.service;

import com.project.demoSpringDiary.model.Day;
import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.model.Meal;
import com.project.demoSpringDiary.repository.DayRepository;
import com.project.demoSpringDiary.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DayService {

    private final DayRepository repository;
    private final MealRepository mealRepository;


    @Autowired
    public DayService(DayRepository repository, MealRepository mealRepository) {
        this.repository = repository;
        this.mealRepository = mealRepository;

    }
    public List<Day> getAll(){
        return (List<Day>) repository.findAll();
    }

    public Double getDailyCalorieBalance (LocalDate date, DiaryUser user){
        return user.getCaloriesForResult() - dailyCalorieConsumption(date, user);
    }

    public Double dailyCalorieConsumption(LocalDate date, DiaryUser user){
        return mealRepository.findByDateAndUser(date, user).stream()
                .map(Meal::getCaloriesPerServing)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public List<Day> sortDaysByCriteria(String criteria, DiaryUser user) {
        if (criteria != null && !criteria.isEmpty()) {
            return switch (criteria) {
                case "dateAsc" -> repository.findByUserOrderByDateFromDiaryAsc(user);
                case "dateDesc" -> repository.findByUserOrderByDateFromDiaryDesc(user);
                default -> getAll();
            };
        } else {
            return getAll();
        }
    }

    public void saveChangedDayCurrentUser(LocalDate date){
        DiaryUser user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (date == null) {
            date = LocalDate.now();
        }
        Day day;
        Optional<Day> optionalDay = repository.findByDateFromDiaryAndUser(date, user);
        if (optionalDay.isPresent()) {
            day = optionalDay.get();
        } else {
            day = new Day(date, user);
        }
        day.setDailyCalorieGoal(user.getCaloriesForResult());
        day.setDailyCalorieConsumption(dailyCalorieConsumption(date, user));
        day.setDailyCalorieBalance(getDailyCalorieBalance(date, user));
        repository.save(day);
    }
}
