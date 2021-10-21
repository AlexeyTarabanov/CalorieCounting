package ru.alex.util;

import ru.alex.model.Meal;
import ru.alex.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.alex.util.TimeUtil.isBetweenHalfOpen;

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

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

        // считаем сумму калорий в день
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<MealTo> mealTos = new ArrayList<>();
        Predicate<Boolean> predicate = b -> true;

        for (Meal meal : meals) {
                // merge (K, V, Function)
                // если даты не существует или количество калорий равно нулю - добавляем пару key-value
                // если дата существует и количество калорий НЕ равно нулю - метод меняет value на результат выполнения функции
                // если Function возвращает null - key удаляется из коллекции.
            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), (a, b) -> Integer.sum(a, b));

            // проверяем находится ли данный отрезок времени в промежутке м/у startTime и endTime
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                predicate = predicate.and(b -> mealTos.add(createTo(meal,
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)));
            }
        }

        predicate.test(true);
        return mealTos;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumByDate = meals
                .stream()
                // получаем 2 даты и количество каллорий за день
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));


        return meals
                .stream()
                .filter(meal ->
                        isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
