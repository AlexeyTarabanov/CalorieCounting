package ru.alex.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 В пакете Model
 находятся объекты, с которыми работает приложение
 (сейчас они находятся в памяти, но затем мы их будем хранить в БД)
 bean Meal (UserMeal)
 хранит еду пользователя, у которой есть
 время, описание и калории
 Meal - entity, которая хранится в БД
 */

public class Meal {

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
