<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ page import="ru.alex.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <%--    stylesheet--%>
    <%--    Определяет, что подключаемый файл хранит таблицу стилей (CSS)--%>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section> <%--
служит для группировки взаимосвязанного содержимого--%>
    <h3><a href="index.html">Home</a></h3>
    <%--h3 - создает заголовок 3-го уровня--%>
    <%--a -  предназначен для создания ссылок--%>
    <hr/>
    <%--рисует горизонтальную линию--%>
    <h2>Meals</h2>
    <%--        устанавливаем форму на веб-странице--%>
        <form method="get" action="meals">
            <input type="hidden" name="action" value="filter">
            <%--        dl создает список--%>
            <dl>
                <%--            <dt> создает термин--%>
                <%--            <dd> задает определение этого термина--%>
                <dt>From-a Date (inclusive)</dt>
                <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
            </dl>
            <dl>
                <dt>To Date (inclusive)</dt>
                <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
            </dl>
            <dl>
                <dt>From Time (включительно)</dt>
                <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
            </dl>
            <dl>
                <dt>To Time (включительно)</dt>
                <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
            </dl>
            <%--        кнопка отправки данных формы на сервер--%>
            <button type="submit">Filter</button>
        </form>
        <hr/>
        <a href="meals?action=create">Add Meal</a>
        <br><br><%--
    устанавливает перевод строки в том месте, где этот тег находится
    в отношении Add Meal таблица спуститься на 1 строку вниз--%>
        <table border="1" cellpadding="8" cellspacing="0"><%--
    table - контейнер для элементов, определяющих содержимое таблицы.
    cellpadding - определяет расстояние между содержимым ячейки таблицы и ее границей
    cellspacing - задаёт расстояние между ячейками таблицыcellspacing - задаёт расстояние между ячейками таблицы--%>
            <thead><%--
        предназначен для хранения одной или нескольких строк, которые представлены вверху таблицы--%>
            <tr><%--
        служит контейнером для создания строки таблицы.--%>
                <th>Date</th>
                <%--
                            предназначен для создания одной ячейки таблицы, которая обозначается как заголовочная
                            Текст в такой ячейке отображается браузером обычно жирным шрифтом и выравнивается по центру
                            (используется для заголовков)--%>
                <th>Description</th>
                <th>Calories</th>
                <th></th>
                <th></th>
                <%--
                            добавил дополнительно 2 пустых ячейки--%>
            </tr>
            </thead>
            <c:forEach items="${meals}" var="meal"><%--
        forEach - позволяет задать цикл (тег из библиотеки JSTL)--%>
                <jsp:useBean id="meal" scope="page" type="ru.alex.to.MealTo"/><%--
            jsp:useBean - объявляем объект класса MealTo
            scope - атрибут, определяющий область видимости ссылки на экземпляр объекта JavaBean
            page - доступен до тех пор, пока не будет отправлен ответ клиенту
            или пока запрос к текущей странице JSP не будет перенаправлен куда-нибудь еще
            type - дает возможность определить тип переменных скрипта как класс, суперкласс или интерфейс, реализуемый классом--%>
<%--                <tr class="${meal.excess ? 'excess' : 'normal'}">--%>
                <tr data-mealExcess="${meal.excess}">
                    <td><%=DateTimeUtil.toString(meal.getDateTime())%>
                    </td>
                        <%--
                                        td - предназначен для создания одной ячейки таблицы--%>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                    <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
                        <%--
                                        заполнил эти ячейки данными--%>
                </tr>
            </c:forEach>
        </table>
</section>
</body>
</html>
