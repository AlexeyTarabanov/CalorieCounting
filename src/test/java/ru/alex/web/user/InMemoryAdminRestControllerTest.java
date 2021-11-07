package ru.alex.web.user;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.alex.repository.inmemory.InMemoryUserRepository;
import ru.alex.util.exception.NotFoundException;

import java.util.Arrays;

import static ru.alex.UserTestData.NOT_FOUND;
import static ru.alex.UserTestData.USER_ID;

/**
 здесь мы тестируем InMemoryAdminRestController
 (по соглашению пишем имя класса, который тестируем и добавляем в конце Test)

 */

public class InMemoryAdminRestControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;
    private static InMemoryUserRepository repository;

    @BeforeClass
    // указывает на то, что метод будет выполнятся в начале всех тестов,
    // а точней в момент запуска тестов(перед всеми тестами @Test).
    public static void beforeClass() {
        // поднимаем контекст Spring'a
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        // достаем из контекста Spring'a и сохраняем в статических членах класса теста
        controller = appCtx.getBean(AdminRestController.class);
        repository = appCtx.getBean(InMemoryUserRepository.class);
    }

    @AfterClass
    // указывает на то, что метод будет выполнятся после всех тестов
    public static void afterClass() { appCtx.close(); }

    @Before
    // указывает на то, что метод будет выполнятся перед каждым тестируемым методом @Test
    public void setUp() {
        // инициализируем данные перед каждым тестом
        // таким образом тест портит состояние хранилища
        // оно будет восстонавливаться перед каждым тестом
        repository.init();
    }

    @Test
    // определяет что метод method() является тестовым
    public void delete() {
        controller.delete(USER_ID);
        // здесь проверяем, что больше его нет в репозитории
        // *** утверждение, что действительно к нам вернулся null из репозитория
        Assert.assertNull(repository.get(USER_ID));
    }

    @Test
    public void deleteNotFound() {
        // здесь мы ожидаем NotFoundException
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND)); }
}
