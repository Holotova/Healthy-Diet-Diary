package com.project.demoSpringDiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.project.demoSpringDiary.repository")
@EntityScan("com.project.demoSpringDiary.model")
@SpringBootApplication
public class DemoSpringDiaryApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoSpringDiaryApplication.class, args);

    }
}
