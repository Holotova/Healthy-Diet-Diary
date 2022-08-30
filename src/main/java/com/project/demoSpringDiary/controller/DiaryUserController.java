package com.project.demoSpringDiary.controller;

import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.model.Role;
import com.project.demoSpringDiary.service.DiaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class DiaryUserController {

    private final DiaryUserService diaryUserService;

    @Autowired
    public DiaryUserController(DiaryUserService diaryUserService) {
        this.diaryUserService = diaryUserService;

    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView getUserList(ModelAndView modelAndView) {
        Iterable<DiaryUser> users = diaryUserService.getAll();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("userList");
        return modelAndView;
    }

    @GetMapping("users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView viewEditUser(@PathVariable("id") String id, ModelAndView modelAndView) {
        DiaryUser user = diaryUserService.getUserById(id);
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", Role.values());
        modelAndView.setViewName("userEdit");
        return modelAndView;
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUser(@RequestParam String username,
                             @RequestParam(name = "roles[]", required = false) String[] roles,
                             @RequestParam("userId") String id) {
       diaryUserService.saveEditUser(username, roles, id);
        return "redirect:/users";
    }

    @GetMapping("/userStatistics")
    public ModelAndView viewUserDailyBalanceWithSortOrFilter(@RequestParam(required = false)
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate filterDate,
                                                             @RequestParam(required = false) String sort,
                                                             ModelAndView modelAndView) {
        modelAndView = diaryUserService.filterOrSortUserBalance(modelAndView, filterDate, sort);
        modelAndView.setViewName("userStatistics");
        return modelAndView;
    }
}

