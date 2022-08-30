package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.service.DiaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BmrController {

    DiaryUserService diaryUserService;

    @Autowired
    public BmrController(DiaryUserService diaryUserService) {
        this.diaryUserService = diaryUserService;
    }

    @PostMapping("/bmr")
    public ModelAndView calculateBmrForGuest(@RequestParam(required = false) Integer weight,
                                             @RequestParam(required = false) Integer height,
                                             @RequestParam(required = false) Integer age,
                                             @RequestParam(required = false) String gender,
                                             @RequestParam(required = false) String activityType,
                                             ModelAndView modelAndView) {

        DiaryUser user = diaryUserService.calculateBmrForGuest(gender, activityType, age, height, weight);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("bmr");
        return modelAndView;
    }

    @GetMapping("/bmr")
    public ModelAndView viewBmrOfGuest(ModelAndView modelAndView) {
        DiaryUser user = new DiaryUser();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("bmr");
        return modelAndView;
    }
}
