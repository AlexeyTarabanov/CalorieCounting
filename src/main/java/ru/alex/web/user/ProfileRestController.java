package ru.alex.web.user;

import ru.alex.model.User;

/**
 Контроллер пользователя.
 Здесь он может посмотреть и модифицировать свои данные
 */

public class ProfileRestController extends AbstractUserController {

    @Override
    public User get(int id) {
        return super.get(id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }
}
