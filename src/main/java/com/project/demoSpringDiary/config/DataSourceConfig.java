package com.project.demoSpringDiary.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        String dbUrlLocal = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "root";

        if (dbUrl != null) {
            try {
                URI dbUri = new URI(dbUrl);
                dbUrlLocal = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +
                        "?sslmode=require";
                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
            } catch (URISyntaxException | NullPointerException e) {
                return null;
            }
        }

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dbUrlLocal);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}
