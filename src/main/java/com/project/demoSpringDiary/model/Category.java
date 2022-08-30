package com.project.demoSpringDiary.model;

public enum Category {
    FRUITS("FRUITS"),
    VEGETABLES("VEGETABLES"),
    MEAT("MEAT"),
    FISH("FISH"),
    MILK("MILK"),
    SWEETS("SWEETS"),
    CEREALS("CEREALS");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
