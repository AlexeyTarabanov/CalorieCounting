package ru.alex.web;

import ru.alex.model.AbstractBaseEntity;

import static ru.alex.util.UserMealsUtil.DEFAULT_CALORIES_PER_DAY;

/**
 LoggedUser
 класс, из которого приложение будет получать данные залогиненного пользователя
 (пока аутентификации нет, он реализован как заглушка).
 */

public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;

    // id залогиненого юзера
    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    // его норма калорий за день
    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}
