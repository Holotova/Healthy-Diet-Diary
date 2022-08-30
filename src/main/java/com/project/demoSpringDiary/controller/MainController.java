package com.project.demoSpringDiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/main")
    public ModelAndView viewMainPage(ModelAndView modelAndView) {
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
