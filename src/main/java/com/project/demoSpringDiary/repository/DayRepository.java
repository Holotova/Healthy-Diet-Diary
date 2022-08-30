package com.project.demoSpringDiary.repository;

import com.project.demoSpringDiary.model.Day;
import com.project.demoSpringDiary.model.DiaryUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface DayRepository extends CrudRepository<Day, String> {
    List<Day> findByUser (DiaryUser user);
    Optional<Day> findByDateFromDiaryAndUser (LocalDate date, DiaryUser user);
    List<Day> findByUserOrderByDateFromDiaryAsc(DiaryUser user);
    List<Day> findByUserOrderByDateFromDiaryDesc(DiaryUser user);

}
