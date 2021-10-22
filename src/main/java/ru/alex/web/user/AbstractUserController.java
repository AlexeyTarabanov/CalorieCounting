package ru.alex.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.model.User;
import ru.alex.service.UserService;

import java.util.List;

import static ru.alex.util.ValidationUtil.assureIdConsistent;
import static ru.alex.util.ValidationUtil.checkNew;

/**
 Абстрактный контроллер c общими методами для контроллеров user и admin.
 от него будут наследоваться другие контроллеры.
 сюда делегируем всю функциональность.
 здесь происходит легирование запроса и делегирование запроса в сервисе
 (MealService и UserService)
 * */

public class AbstractUserController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }
}
