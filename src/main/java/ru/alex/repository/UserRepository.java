package ru.alex.repository;

import org.springframework.stereotype.Repository;
import ru.alex.model.User;

import java.util.List;

/**
 работа с юзерами
 */

public interface UserRepository {

    // null, если не найден, при обновлении
    User save(User user);

    // false, если не найден
    boolean delete(int id);

    // mull, если не найден
    User get(int id);

    // null, если не найден
    User getByEmail(String email);

    List<User> getAll();
}
