package ru.alex;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.alex.model.Role;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.repository.inmemory.InMemoryUserRepository;
import ru.alex.service.UserService;
import ru.alex.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {

        // через класс ClassPathXmlApplicationContext, Spring поднимает свои бины
        // на основе xml конфигурации, которая находится в ClassPath-е
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {

            System.out.println("массив имен бинов, которые находятся в контексте: " +
                    Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminRestController = appCtx.getBean(AdminRestController.class);
            adminRestController.create(
                    new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
        }
    }
}
