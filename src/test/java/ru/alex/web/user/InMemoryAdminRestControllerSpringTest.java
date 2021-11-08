package ru.alex.web.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alex.repository.inmemory.InMemoryUserRepository;
import ru.alex.util.exception.NotFoundException;

import static ru.alex.UserTestData.NOT_FOUND;
import static ru.alex.UserTestData.USER_ID;

@ContextConfiguration("classpath:spring/spring-app.xml")
// аннотация определяет метаданные уровня класса,
// которые используются для определения того, как загружать и настраивать ApplicationContext для интеграционных тестов.
// В частности, @ContextConfiguration объявляет расположение ресурсов контекста приложения или классы компонентов,
// используемые для загрузки контекста.
// (из classpath'a берем context)
@RunWith(SpringRunner.class)
// JUnit будет запускать тесты в классе, который указан в параметрах этой аннотации,
// вместо стандартного раннер а, встроенного в JUnit.
public class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private AdminRestController controller;

    @Autowired
    private InMemoryUserRepository repository;

    @Before
    // позволяет выполнять логику перед выполнением каждого теста
    public void setUp() {
        repository.init();
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        // здесь проверяем, что больше его нет в репозитории
        // *** утверждение, что действительно к нам вернулся null из репозитория
        Assert.assertNull(repository.get(USER_ID));
    }

    @Test
    public void deleteNotFound() {
        // здесь мы ожидаем NotFoundException
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}
