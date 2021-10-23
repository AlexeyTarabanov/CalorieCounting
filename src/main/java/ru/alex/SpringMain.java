package ru.alex;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.alex.model.Role;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.repository.inmemory.InMemoryUserRepository;
import ru.alex.service.UserService;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {

        // через класс ClassPathXmlApplicationContext, Spring поднимает свои бины
        // на основе xml конфигурации, которая находится в ClassPath-е
        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext("spring/spring-app.xml");

        System.out.println("массив имен бинов, которые находятся в контексте: " +
                Arrays.toString(appCtx.getBeanDefinitionNames()));

        // получаем bean
        UserRepository userRepository = (UserRepository) appCtx.getBean("inMemoryUserRepository");
        // и у него уже можем вызвать нужные нам методы
        userRepository.getAll();

        UserService userService = appCtx.getBean(UserService.class);
        userService.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
        
        appCtx.close();
    }
}
