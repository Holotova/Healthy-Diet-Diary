package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.Meal;
import com.project.demoSpringDiary.service.DayService;
import com.project.demoSpringDiary.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class DayController {

    MealService mealService;
    DayService dayService;

    @Autowired
    public DayController(MealService mealService, DayService dayService) {
        this.mealService = mealService;
        this.dayService = dayService;
    }


    @GetMapping("/personalDiary")
    @PreAuthorize("hasAuthority('USER')")
    public ModelAndView showMeal(@RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                 ModelAndView modelAndView) {
        Iterable<Meal> meals = mealService.getMealsByDateCurrentUser(date);
        modelAndView.addObject("meals", meals);
        modelAndView.addObject("date", date);
        modelAndView.setViewName("personalDiary");
        return modelAndView;
    }

    @PostMapping("/personalDiary/meals")
    public ModelAndView saveDailyMeal(@RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                      @RequestParam String productName,
                                      @RequestParam Double weight, ModelAndView modelAndView,
                                      RedirectAttributes redirAttrs) {
        if (!mealService.isSaveDailyMeal(productName, weight, date)) {
            redirAttrs.addFlashAttribute("message", "Product not found, " +
                    "please detail your request or check for suitable product in Product List");
            modelAndView.setViewName("redirect:/personalDiary");
        }
        modelAndView.setViewName("redirect:/personalDiary");
        return modelAndView;
    }

    @PostMapping("/personalDiary/meals/edit")
    public String editMeal(@RequestParam Double weight,
                           @RequestParam(value = "id") String id) {
        mealService.saveEditMeal(weight, id);
        return "redirect:/personalDiary";
    }

    @GetMapping("/personalDiary/meals/{id}")
    public ModelAndView editMeal(@PathVariable("id") String id, ModelAndView modelAndView) {
        Meal meal = mealService.getMealById(id);
        modelAndView.addObject("meal", meal);
        modelAndView.setViewName("mealEdit");
        return modelAndView;
    }

    @PostMapping("/personalDiary")
    public ModelAndView saveUserDay(@RequestParam(required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                    ModelAndView modelAndView) {
        dayService.saveChangedDayCurrentUser(date);
        modelAndView.setViewName("redirect:/userStatistics");
        return modelAndView;
    }

}
