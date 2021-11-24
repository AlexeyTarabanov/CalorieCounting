package ru.alex.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alex.model.Meal;
import ru.alex.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.alex.MealTestData.*;
import static ru.alex.UserTestData.ADMIN_ID;
import static ru.alex.UserTestData.USER_ID;

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
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        // если еда с этим id чужая или отсутствует - NotFoundException.
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        // тест на удаление еды, которая отсутствует в БД
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        // тест на удаление еды, которая не принадлежит
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        // при выполнении функционала create/update объекты могут измениться, и мы не можем считать их эталонными.
        // поэтому при сравнении мы создаем эталон еще раз.
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);

        MEAL_MATCHER.assertMatch(created, newMeal);
        // если еда с этим id чужая или отсутствует - NotFoundException.
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        // тест на создание одного и того же времени
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        // подтверждает, что утверждение выполнено успешно, поскольку данные обоих объектов совпадают
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        // тест get на отстутствующую еду
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        // тест get на еду, которая не принадлежит никому
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        // подтверждает, что утверждение выполнено успешно, поскольку данные обоих объектов совпадают
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void getAll() {
        // берем всю еду из списка
        // и сравниваем ее с образцом
        // проверяем, что она вернулась строго в этом порядке
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void getBetweenInclusive() {
        // тест на получение отфильтрованной еды
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        // тест на получение отфильтрованной еды со значением null
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), meals);
    }
}