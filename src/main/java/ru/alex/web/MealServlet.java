package ru.alex.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import ru.alex.model.Meal;
import ru.alex.repository.inmemory.InMemoryMealRepository;
import ru.alex.repository.MealRepository;
import ru.alex.util.UserMealsUtil;
import ru.alex.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.alex.util.DateTimeUtil.parseLocalDate;
import static ru.alex.util.DateTimeUtil.parseLocalTime;

public class MealServlet extends HttpServlet {

    // через класс ConfigurableApplicationContext, Spring поднимает свои бины
    // на основе xml конфигурации, которая находится в ClassPath-е
    private ConfigurableApplicationContext springContext;
    // Сервлет обращается к контролеру, контроллер вызывает сервис, сервис - репозиторий
    private MealRestController mealController;


    @Override
    // Вызывается контейнером сервлета, чтобы указать сервлету, что сервлет вводится в эксплуатацию
    public void init() throws ServletException {
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = springContext.getBean(MealRestController.class);
    }

    @Override
    // Вызывается контейнером сервлета, чтобы указать сервлету, что сервлет выводится из эксплуатации.
    public void destroy() {
        springContext.close();
        super.destroy();
    }

    // обрабатывает запросы POST (отправка данных)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // setCharacterEncoding
        // - заменяет имя кодировки, используемой в тексте запроса
        // метод должен быть вызван перед чтением параметров запроса
        req.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));

        // возвращает: истина, если строка не равна нулю, ее длина больше 0 и она не содержит только пробелов.
        if(StringUtils.hasLength(req.getParameter("id"))) {
            mealController.update(meal, getId(req));
        } else {
            mealController.create(meal);
        }

        // переадресовываем на meals
        resp.sendRedirect("meals");
    }

    @Override
    // получение данных
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // запрашиваем action
        String action = req.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(req);
                mealController.delete(id);
                // переадресовывает запрос на meals
                resp.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        // localTime дает время в формате hh:mm:ss, nnn
                        // localTime.truncatedTo(ChronoUnit.MINUTES) - сокращает до минут
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealController.get(getId(req));
                // получем meal
                req.setAttribute("meal", meal);
                // перенаправляем запрос на /mealForm.jsp
                req.getRequestDispatcher("/mealForm.jsp").forward(req, resp);
                break;
            case "filter":
                LocalDate startDate = parseLocalDate(req.getParameter("startDate"));
                LocalDate endDate = parseLocalDate(req.getParameter("endDate"));
                LocalTime startTime = parseLocalTime(req.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(req.getParameter("endTime"));
                req.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            case "all":
            default:
                req.setAttribute("meals",
                        mealController.getAll());
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
                break;
        }
    }

    // получаем id из запроса
    private int getId(HttpServletRequest request) {
        // requireNonNull - проверяет, что указанная ссылка на объект не пустая
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
