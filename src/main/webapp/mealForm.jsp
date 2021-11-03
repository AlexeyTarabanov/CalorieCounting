<%--
  здесь будет форма для редактирования
  при нажатии на «кнопку» add или update будет отображаться эта форма
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
<%--    stylesheet--%>
<%--    Определяет, что подключаемый файл хранит таблицу стилей (CSS)--%>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section><%--
служит для группировки взаимосвязанного содержимого--%>
    <h3><a href="index.html">HOME</a> </h3>
    <hr>
    <h2>${param.action == 'create' ? 'Create meal' : 'Edit meal'}</h2><%--
    зависимости от переданного параметра action будет отображаться страница
    Create meal (при нажатии на ссылку Add Meal)
    либо Edite meal (при нажатии на ссылку Update)--%>
    <jsp:useBean id="meal" type="ru.alex.model.Meal" scope="request"/><%--
    объявил объект JavaBean, который будет использоваться на странице JSP--%>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}"><%--
    устанавливаем форму на веб-странице--%>
        <dl><%--
        с помощью тега dl создаем список--%>
            <dt>DateTime</dt><%--
            создает термин--%>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd><%--
            определение термина
            input - задает элементы формы
            required - обязательное для заполнения поле
            --%>
        </dl>
        <dl>
            <dt>Description</dt>
            <dd><input type="text" value="${meal.description}" size="40" name="description" required></dd>
        </dl>
        <dl>
            <dt>Calories</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit">Save</button><%--
        button - создает  кнопки
        с атрибутом type="button | reset | submit").
        button - обычная кнопка
        reset - кнопка для очистки введенных данных формы и возвращения значений в первоначальное состояние
        submit - кнопка для отправки данных формы на сервер--%>
        <button onclick="window.history.back()" type="button">Cancel</button><%--
        событие onclick возникает при щелчке левой кнопкой мыши на элементе, к которому добавлен атрибут onclick.
        window.history.back() -
        используется для возврата на предыдущую страницу в истории текущего сеанса
        Если предыдущей страницы нет, этот вызов метода ничего не делает--%>
    </form>
</section>
</body>
</html>
