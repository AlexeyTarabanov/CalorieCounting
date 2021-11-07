package ru.alex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.alex.model.Meal;
import ru.alex.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.alex.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.alex.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.alex.util.ValidationUtil.checkNotFoundWithId;

/**
 находится между контроллером и репозиторием
 реализует всю необходимую логику приложения, все вычисления, взаимодействует с БД
 и передает уровню представления (Controller) результат обработки.
 */

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        // если еда с этим id чужая или отсутствует - NotFoundException.
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        // если еда с этим id чужая или отсутствует - NotFoundException.
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void update(Meal meal, int userId) {
        // если обновляемая еда с этим id чужая или отсутствует - NotFoundException
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    // получить всю еду
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    // получить отфильтрованную еду по startDate, startTime, endDate, endTime
    public List<Meal> getBetweenInclusive (@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }
}
