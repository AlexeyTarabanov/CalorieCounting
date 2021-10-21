package ru.alex.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // получаем RequestDispatcher (Диспетчер Запросов)
        // и указываем ему jsp страницу,  которая будет отображаться при обращении к данному методу GET.
        // Метод forward(req, resp) перенаправляет наш запрос на jsp страницу.

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
        response.sendRedirect("users.jsp");
    }
}
