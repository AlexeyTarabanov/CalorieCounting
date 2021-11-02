package ru.alex.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    // нужны для тестов
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    @Override
    public List<User> getAll() {
        return getCollection()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return getCollection()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
