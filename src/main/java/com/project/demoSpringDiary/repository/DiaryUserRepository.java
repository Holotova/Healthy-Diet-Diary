package com.project.demoSpringDiary.repository;

import com.project.demoSpringDiary.model.DiaryUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryUserRepository extends CrudRepository<DiaryUser, String> {

    DiaryUser findByUserName(String userName);


}
