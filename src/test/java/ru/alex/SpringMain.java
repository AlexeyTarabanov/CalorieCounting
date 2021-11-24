package ru.alex;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.alex.model.Role;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;
import ru.alex.repository.inmemory.InMemoryUserRepository;
import ru.alex.service.UserService;
import ru.alex.to.MealTo;
import ru.alex.web.meal.MealRestController;
import ru.alex.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {

        // через класс ClassPathXmlApplicationContext, Spring поднимает свои бины
        // на основе xml конфигурации, которая находится в ClassPath-е
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml", "classpath:spring/inmemory.xml")) {

            System.out.println("массив имен бинов, которые находятся в контексте: " +
                    Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminRestController = appCtx.getBean(AdminRestController.class);
            adminRestController.create(
                    new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess =
                    mealController.getBetween(
                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);
            System.out.println();
            System.out.println(mealController.getBetween(null, null, null, null));
        }
    }
}
