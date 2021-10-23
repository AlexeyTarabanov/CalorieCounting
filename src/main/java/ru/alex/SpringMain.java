package ru.alex;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.alex.repository.UserRepository;
import ru.alex.repository.inmemory.InMemoryUserRepository;

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
        UserRepository userRepository = appCtx.getBean(UserRepository.class);
        // и у него уже можем вызвать нужные нам методы
        userRepository.getAll();
        appCtx.close();
    }
}
