package com.project.demoSpringDiary.service;

import com.project.demoSpringDiary.model.ActivityType;
import com.project.demoSpringDiary.model.Day;
import com.project.demoSpringDiary.model.DiaryUser;
import com.project.demoSpringDiary.model.Role;
import com.project.demoSpringDiary.repository.DayRepository;
import com.project.demoSpringDiary.repository.DiaryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Service
public class DiaryUserService implements UserDetailsService {
    private static final Integer COEFFICIENT_FOR_WEIGHT = 10;
    private static final Double COEFFICIENT_FOR_HEIGHT = 6.25;
    private static final Integer COEFFICIENT_FOR_AGE = 5;
    private static final Integer COEFFICIENT_FOR_MAN = 5;
    private static final Integer COEFFICIENT_FOR_WOMAN = -161;
    private static final Integer CALORIES_FOR_ONE_KG = 7000;


    private final DiaryUserRepository repository;
    private final DayRepository dayRepository;
    private final DayService dayService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public DiaryUserService(DiaryUserRepository repository, DayRepository dayRepository, DayService dayService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.dayRepository = dayRepository;
        this.dayService = dayService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return repository.findByUserName(userName);
    }

    public DiaryUser getUserById(String id) {
        Optional<DiaryUser> optional = repository.findById(id);
        DiaryUser user;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException(" Meal not found for id : " + id);
        }
        return user;
    }

    public Iterable<DiaryUser> getAll() {
        return repository.findAll();
    }

    public boolean addUser(DiaryUser user){
        DiaryUser userFromDb = repository.findByUserName(user.getUsername());
        if(userFromDb != null){
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDay(new Day());
        repository.save(user);
        return true;
    }

    public Double getBasalMetabolicRate(DiaryUser user) {
        Double bmr;
        if (user.getIsFemale()) {
            bmr = (COEFFICIENT_FOR_WEIGHT * user.getWeight() + COEFFICIENT_FOR_HEIGHT * user.getHeight() -
                    COEFFICIENT_FOR_AGE * user.getAge() +
                    COEFFICIENT_FOR_WOMAN) * ActivityType.valueOf(user.getActivityType().name()).getActivityValue();
        } else {
            bmr = (COEFFICIENT_FOR_WEIGHT * user.getWeight() + COEFFICIENT_FOR_HEIGHT * user.getHeight() -
                    COEFFICIENT_FOR_AGE * user.getAge() +
                    COEFFICIENT_FOR_MAN) * ActivityType.valueOf(user.getActivityType().name()).getActivityValue();
        }
        return bmr;
    }

    public DiaryUser calculateBmrForGuest(String gender, String activityType, Integer age,
                                          Integer height, Integer weight){
        Boolean isFemale = gender.equalsIgnoreCase("female");
        ActivityType type = ActivityType.valueOf(activityType);
        DiaryUser user = new DiaryUser(isFemale, age, height, weight, type);
        Double bmr = getBasalMetabolicRate(user);
        user.setBasalMetabolicRate(bmr);
        return user;
    }

    public Double getCaloriesAccordingGoal(DiaryUser user) {
        user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer kgsToGoal = user.getGoalWeight() - user.getWeight();
        Double dailyCalorieDifference = Double.valueOf(kgsToGoal * CALORIES_FOR_ONE_KG / user.getDaysForResult());
        Double caloriesForResult = getBasalMetabolicRate(user) + dailyCalorieDifference;
        user.setCaloriesForResult(caloriesForResult);
        return caloriesForResult;
    }

    public DiaryUser calculateAndSaveBmrForLoggedUser(String gender, String activityType, Integer age,
                                                      Integer height, Integer weight, Integer goalWeight,
                                                      Integer daysForResult){
        DiaryUser user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isFemale = gender.equalsIgnoreCase("FEMALE");
        ActivityType type = ActivityType.valueOf(activityType);
        user.setWeight(weight);
        user.setHeight(height);
        user.setAge(age);
        user.setIsFemale(isFemale);
        user.setActivityType(type);
        user.setGoalWeight(goalWeight);
        user.setDaysForResult(daysForResult);
        Double bmr = getBasalMetabolicRate(user);
        user.setBasalMetabolicRate(bmr);
        user.setCaloriesForResult(getCaloriesAccordingGoal(user));
        repository.save(user);
        return user;
    }

    public ModelAndView filterOrSortUserBalance(ModelAndView modelAndView, LocalDate filterDate, String sort) {
        Predicate<LocalDate> isFiltered = x -> x != null && !x.toString().isEmpty();
        Predicate<String> isSorted = y -> y != null && !y.isEmpty();
        DiaryUser user = (DiaryUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Day> diary = new ArrayList<>();
        String message = "You don't have any records. Go to Personal Diary page, add meals and save Day Changes";
        if (isFiltered.test(filterDate)) {
            Optional<Day> optionalDay = dayRepository.findByDateFromDiaryAndUser(filterDate, user);
            if (optionalDay.isPresent()) {
                diary = Collections.singletonList(optionalDay.get());
            } else {
                modelAndView.addObject("message", message);
            }
        } else if (isSorted.test(sort)) {
            diary = dayService.sortDaysByCriteria(sort, user);
        } else {
            diary = dayRepository.findByUser(user);
        }
        if (diary.isEmpty()) {
            modelAndView.addObject("message", message);
        }
        modelAndView.addObject("diary", diary);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    public void saveEditUser(String username, String[] roles, String id){
        DiaryUser user = getUserById(id);
        user.setUserName(username);
        user.getRoles().clear();
        if (roles != null) {
            Arrays.stream(roles).forEach(r -> user.getRoles().add(Role.valueOf(r)));
        }
        repository.save(user);
    }
}
