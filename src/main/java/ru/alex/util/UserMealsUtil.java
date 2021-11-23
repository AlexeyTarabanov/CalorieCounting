package ru.alex.util;

import ru.alex.model.Meal;
import ru.alex.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.alex.util.Util.isBetweenHalfOpen;

public class UserMealsUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {

        Map<LocalDate, Integer> caloriesSumByDate = meals
                .stream()
                // collect - представление результатов в виде коллекций и других структур данных
                .collect(
                        // (группируем по дате) - получаем 2 даты и количество калорий /список еды с характеристиками/
                        Collectors.groupingBy(meal1 -> meal1.getDate(),
                                Collectors.summingInt(meal2 -> meal2.getCalories())));

        return meals
                .stream()
                // filter - отфильтровывает записи, возвращает только записи, соответствующие условию
                .filter(filter)
                // map -преобразует каждый элемент коллекции во что-то другое и на выходе получить новую коллекцию
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                // переводим stream обратно в коллекцию
                .collect(Collectors.toList());

    }

    // без фильтрации по времени
    // дополнительный вспомогательный метод
    // подает туда минимальное и максимальное время для того, чтобы там не происходила фильтрация
    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    // с фильтрацией по времени
    public static List<MealTo> getFilteredTos(List<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
