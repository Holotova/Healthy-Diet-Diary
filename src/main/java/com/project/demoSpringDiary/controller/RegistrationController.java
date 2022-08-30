package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.service.DiaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private final DiaryUserService userService;

    @Autowired
    public RegistrationController(DiaryUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ModelAndView viewRegistration(ModelAndView modelAndView) {
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping("/registration")
    public String addUser(DiaryUser user, ModelAndView modelAndView, RedirectAttributes redirAttrs) {
        if (!userService.addUser(user)) {
            redirAttrs.addFlashAttribute("message", "User already exists!");
            return "redirect:/registration";
        }
        return "redirect:/login";
    }
}
