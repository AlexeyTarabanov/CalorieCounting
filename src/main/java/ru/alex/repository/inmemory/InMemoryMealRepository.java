package ru.alex.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
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

    private final Map<Integer, Map<Integer, Meal>> usersMealsMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserMealsUtil.meals.forEach(meal -> save(meal, USER_ID));
        save(new Meal(LocalDateTime.of(2021, Month.OCTOBER, 29, 14, 0), "Админ ланч", 510), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2021, Month.OCTOBER, 29, 21, 0), "Админ ужин", 1500), ADMIN_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        // computeIfAbsent - если значения нет, выполняем функцию
        // если этого юзера нет, создаем новый
        Map<Integer, Meal> meals = usersMealsMap.computeIfAbsent(userId, id -> new ConcurrentHashMap<>(id));
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // добавит новый элемент в Map, если элемент с таким ключом там отсутствует
        // в качестве value ему будет присвоен результат выполнения функции mappingFunction.
        // если элемент уже есть — он не будет перезаписан, а останется на месте.
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    // теперь все методы будут начинаться с метода get()
    // получаем мапу еды для конкретного юзера
    // если этого юзера нет, создаем новый ConcurrentHashMap
    // метод save()

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
        if (meals != null) {
            if (meals.remove(id) != null)
                return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
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
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
        return meals.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}
