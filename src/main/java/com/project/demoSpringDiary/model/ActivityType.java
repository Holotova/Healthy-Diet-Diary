package com.project.demoSpringDiary.model;

public enum ActivityType {
    MINIMAL ("MINIMAL", 1.2),
    LOW ("LOW", 1.375),
    MILD ("MILD", 1.55),
    HEAVY ("HEAVY", 1.725),
    EXTREME ("EXTREME", 1.9);


    private final String activityName;
    private final Double activityValue;

    ActivityType(String activityName, Double activityValue) {
        this.activityName = activityName;
        this.activityValue = activityValue;
    }

    public Double getActivityValue() {
        return activityValue;
    }
}
