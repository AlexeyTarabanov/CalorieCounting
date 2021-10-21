package ru.alex.web;

import org.slf4j.Logger;
import ru.alex.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setAttribute("meals", UserMealsUtil.getTos(UserMealsUtil.meals, UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
