package ru.alex.web;

import static ru.alex.util.UserMealsUtil.DEFAULT_CALORIES_PER_DAY;

/**
 LoggedUser
 класс, из которого приложение будет получать данные залогиненного пользователя
 (пока аутентификации нет, он реализован как заглушка).
 */

public class SecurityUtil {

    // id залогиненого юзера
    public static int authUserId() {
        return 1;
    }

    // его норма калорий за день
    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}
