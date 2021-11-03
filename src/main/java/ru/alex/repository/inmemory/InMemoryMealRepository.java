package ru.alex.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.alex.model.Meal;
import ru.alex.repository.MealRepository;
import ru.alex.util.UserMealsUtil;
import ru.alex.util.Util;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.alex.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static ru.alex.repository.inmemory.InMemoryUserRepository.USER_ID;

/**
 * Здесь будем хранить данные о еде
 * Такая структура данных удобнее, в отличии от списка
 * здесь не нужно каждый раз итерироваться для поиска элемента, а
 * достаточно воспользоваться ключом для поиска эл-тов
 */

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, InMemoryBaseRepository<Meal>> usersMealsMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserMealsUtil.meals.forEach(meal -> save(meal, USER_ID));
        save(new Meal(LocalDateTime.of(2021, Month.OCTOBER, 29, 14, 0), "Админ ланч", 510), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2021, Month.OCTOBER, 29, 21, 0), "Админ ужин", 1500), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2021, Month.NOVEMBER, 04, 12, 04), "Админ попил чаек", 100), ADMIN_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        // computeIfAbsent - если значения нет, выполняем функцию
        // если этого юзера нет, создаем новый
        InMemoryBaseRepository<Meal> meals = usersMealsMap.computeIfAbsent(userId, uid -> new InMemoryBaseRepository<>());
        return meals.save(meal);
    }

    // теперь все методы будут начинаться с метода get()
    // получаем мапу еды для конкретного юзера
    // если этого юзера нет, создаем новый ConcurrentHashMap
    // метод save()

    @Override
    public boolean delete(int id, int userId) {
        InMemoryBaseRepository<Meal> meals = usersMealsMap.get(userId);
        if (meals != null && meals.delete(id)) return true;
        else return false;
    }

    @Override
    public Meal get(int id, int userId) {
        InMemoryBaseRepository<Meal> meals = usersMealsMap.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return filterByPredicate(userId, meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDateTime, endDateTime));
    }

    // метод полностью описывает функционал методов getAll() и getBetweenHalfOpen(),
    // разница последнего только в том , что у него есть фильтрация isBetweenHalfOpen, поэтому
    // в методе getAll() Predicate всегда будет true (мы ничего не фильтруем)
    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        InMemoryBaseRepository<Meal> meals = usersMealsMap.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.getCollection()
                .stream()
                .filter(filter)
                // сортируем по времени в обратном порядке
                .sorted(Comparator.comparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }
}
