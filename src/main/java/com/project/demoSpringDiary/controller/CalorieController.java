package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.service.DiaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalorieController {

    DiaryUserService diaryUserService;

    @Autowired
    public CalorieController(DiaryUserService diaryUserService) {
        this.diaryUserService = diaryUserService;
    }

    @PostMapping("/calorieCalculator")
    @PreAuthorize("hasAuthority('USER')")
    public String saveUserWithData(@RequestParam(required = false) String gender,
                                   @RequestParam(required = false) Integer age,
                                   @RequestParam(required = false) Integer height,
                                   @RequestParam(required = false) Integer weight,
                                   @RequestParam(required = false) Integer goalWeight,
                                   @RequestParam(required = false) Integer daysForResult,
                                   @RequestParam(required = false) String activityType,
                                   ModelAndView modelAndView) {
        DiaryUser user = diaryUserService.calculateAndSaveBmrForLoggedUser(gender, activityType, age, height,
                weight, goalWeight, daysForResult);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("calorieCalculator");
        return "redirect:/userStatistics";
    }

    @GetMapping("/calorieCalculator")
    @PreAuthorize("hasAuthority('USER')")
    public ModelAndView showCalorieCalculator(ModelAndView modelAndView) {
        modelAndView.setViewName("calorieCalculator");
        return modelAndView;
    }
}
