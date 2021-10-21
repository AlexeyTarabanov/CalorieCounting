package ru.alex.util;

import ru.alex.model.Meal;
import ru.alex.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

        // считаем сумму калорий в день
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (Meal meal : meals) {
            // получаем дату
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            // калории
            int userMealCalories = meal.getCalories();
            // по ключу-дате получаем калории,
            // если такая дата там присутствует, то к ее значению прибавляем калории
            caloriesSumByDate.put(mealDate, caloriesSumByDate.getOrDefault(mealDate, 0) + userMealCalories);
        }

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (Meal meal : meals) {
            // проверяем находится ли данный отрезок времени в промежутке м/у startTime и endTime
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                // получаем сумму каллорий
                Integer numberOfCalories = caloriesSumByDate.get(meal.getDateTime().toLocalDate());
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), numberOfCalories > caloriesPerDay));
            }
        }

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams

        Map<LocalDate, List<Meal>> listOfMealByDate = meals
                .stream()
                // получаем 2 даты и список еды с характеристиками
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()));


        Map<LocalDate, Integer> caloriesSumByDate = meals
                .stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        // получаем 2 даты и количество каллорий за день
                        Collectors.summingInt(Meal::getCalories)));

        List<UserMealWithExcess> userMealWithExcesses = meals
                .stream()
                .filter(meal ->
                        TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        return userMealWithExcesses;
    }
}
