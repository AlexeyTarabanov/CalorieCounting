package ru.alex.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.alex.model.User;
import ru.alex.repository.UserRepository;

import java.util.List;

import static ru.alex.util.ValidationUtil.checkNotFound;
import static ru.alex.util.ValidationUtil.checkNotFoundWithId;

/**
 UserService логика совсем небольшая, а проверка, что
 null или false, которые к нам пришли из репозитория превращаются в бизнес exception
 Для этого сделал специальный класс NotFoundException, а проверку результатов делаем в классе ValidationUtil
 Так как логика совсем небольшая, то ее можно было бы разместить в контроллерах,
 но здесь мы делаем полное приложение со всеми слоями.
 Поэтому это у нас отдельный слой
 */

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.getId());
    }
}
