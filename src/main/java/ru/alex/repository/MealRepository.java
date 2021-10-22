package ru.alex.repository;

import ru.alex.model.Meal;

import java.util.Collection;

/**
 Здесь будем хранить еду
 */

public interface MealRepository {

    // обслуживает оба случая (create and update)
    // null if updated meal do not belong to userId
    Meal save(Meal meal);

    // false if meal do not belong to userId
    boolean delete(int id);

    // null if meal do not belong to userId
    Meal get(int id);

    // ORDERED dateTime desc
    Collection<Meal> getAll();
}
