package ru.alex.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.alex.UserTestData;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.alex.UserTestData.admin;
import static ru.alex.UserTestData.user;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    public void init() {
        // чистим мапу
        map.clear();
        put(user);
        put(admin);
        counter.getAndSet(UserTestData.ADMIN_ID + 1);
    }

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
