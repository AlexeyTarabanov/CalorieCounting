package ru.alex.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;

import java.util.List;

/**
 реализация UserRepository с помощью JdbcTemplate, которая работает с БД
 */

@Repository
public class JdbcUserRepository implements UserRepository {

    // преобразует строку в новый экземпляр указанного сопоставленного целевого класса
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    // это центральный класс в базовом пакете JDBC
    // упрощает использование JDBC и помогает избежать распространенных ошибок
    // он выполняет основной рабочий процесс JDBC, оставляя код приложения для предоставления SQL и извлечения результатов.
    private final JdbcTemplate jdbcTemplate;

    // расширяет класс JdbcTemplate и инкапсулирует класс JdbcTemplate
    // для поддержки функции именованных параметров
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // многопоточный многоразовый объект, обеспечивающий простую вставку в таблицу.
    // он обеспечивает обработку метаданных для упрощения кода, необходимого для создания базового оператора вставки.
    // все, что вам нужно предоставить, - это имя таблицы и карта, содержащая имена столбцов и значения столбцов.
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        // создаем SimpleJdbcInsert с таблицей users используя генератор ключей id
        // то есть при вставке мы primary key не задаем - он автоматически генерируется
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        // MapSqlParameterSource - реализация SqlParameterSource, которая содержит заданную карту параметров.
        // этот класс предназначен для передачи простой карты значений параметров методам класса NamedParameterJdbcTemplate.
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        // так как метод save выполняет и функцию update - делаем проверку
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map) == 0) {
            return null;
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }
}
