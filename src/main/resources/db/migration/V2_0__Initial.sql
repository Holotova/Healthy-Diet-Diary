create table if not exists diary_user
(
    id varchar(255) not null primary key,
    password varchar(255),
    user_name varchar(255),
    is_female boolean,
    age integer,
    height integer,
    weight integer,
    activity_type varchar(255),
    basal_metabolic_rate real,
    goal_weight integer,
    days_for_result integer,
    calories_for_result real
);

create table if not exists day
(
    id varchar(255) not null primary key,
    date_from_diary date,
    daily_calorie_goal real,
    daily_calorie_consumption real,
    daily_calorie_balance real
);

create table if not exists diary_user_role
(
    diary_user_id varchar(255) not null,
    roles varchar(255)
);


create table if not exists product
(
    id varchar(255) not null primary key,
    product_name varchar(255),
    category varchar(255),
    calories_per100g real,
    weight real
);

create table if not exists meal
(
    id varchar(255) not null primary key,
    weight real,
    calories_per_serving real
);

