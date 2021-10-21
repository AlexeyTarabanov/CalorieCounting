package ru.alex.model;

import java.time.LocalDateTime;

/**
 bean MealTo (UserMealWithExcess) -  DTO (Data Transfer Object)
 объект, преобразуется в другой более удобный для отображения на Presentation Layer (папка web)
 excess - показывает превышает ли сумма каллорий в день заданное значение
 */

public class MealTo {

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
