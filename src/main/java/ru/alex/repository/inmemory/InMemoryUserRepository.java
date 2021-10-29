package ru.alex.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    // нужны для тестов
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    private final Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger();

    @Override
    // false, если не найден
    public boolean delete(int id) {
        log.info("delete {}", id);
        if (usersMap.remove(id) != null) return true;
        else return false;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            usersMap.put(user.getId(), user);
            return user;
        }
        //return usersMap.put(user.getId(), user);

        // позволяет не добавлять юзера, если он уже удалился
        // если пользователь пришел с id, которого нет в мапе - он так же не будет добавляться
        return usersMap.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return usersMap.values()
                .stream()
                // сортируем по имени и email
                .sorted(Comparator.comparing((User user) -> user.getName())
                        .thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return usersMap.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                // получаем первый результат из списка
                .findFirst()
                .orElse(null);
    }
}
