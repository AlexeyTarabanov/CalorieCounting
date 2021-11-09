package ru.alex.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alex.UserTestData;
import ru.alex.model.Role;
import ru.alex.model.User;
import ru.alex.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.alex.UserTestData.*;

// добавляем context Spring'a
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
// тесты будем запускать в SpringRunner
@RunWith(SpringRunner.class)
// @Sql - позволяет выполнять код sql
// spring находит в ресурсах в каталоге db наш файл поуляция БД (/populateDB.sql)
// считывает ее с кодировкой UTF-8
// и исполняет ее перед каждым тестом
// перед каждым тестом БД будето восстанавливаться
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    // при выполнении функционала create/update объекты могут измениться, и мы не можем считать их эталонными.
    // поэтому при сравнении мы создаем эталон еще раз.
    public void create() {
        User created = service.create(getNew());
        Integer newId = created.getId();
        User newUser = getNew();
        newUser.setId(newId);
        // проверяю результат напрямую (не через getAll)
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    // тест на создание юзера с дублирующим email-om
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(USER_ID);
        // проверяю результат напрямую (не через getAll)
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    // тест на удаление юзера, который отсутствует в БД
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        // user - то, что ожидаем
        // UserTestData - то, что есть на самом деле
        // Assert.assertEquals(user, UserTestData.user);
        assertMatch(user, UserTestData.user);
    }

    @Test
    // тест get на отстутствующего юзера
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        assertMatch(user, admin);
    }

    @Test
    public void update() {
        User updated = getUpdated();
        service.update(updated);
        assertMatch(service.get(USER_ID), getUpdated());
    }

    @Test
    public void getAll() {
        // берем всех юзеров из базы
        List<User> all = service.getAll();
        // и сравниваем их с образцом
        // проверяем, что они вернулись строго в этом порядке
        assertMatch(all, admin, user);
    }
}