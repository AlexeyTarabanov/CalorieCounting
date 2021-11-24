package ru.alex.repository.inmemory;

import ru.alex.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.alex.model.AbstractBaseEntity.START_SEQ;

public class InMemoryBaseRepository <T extends AbstractBaseEntity> {

    static final AtomicInteger counter = new AtomicInteger(START_SEQ);
    final Map<Integer, T> map = new ConcurrentHashMap<>();

    public T save(T entity) {
        // проверяет, что указанная ссылка на объект не является нулевой,
        // и выдает настраиваемое исключение NullPointerException, если это так.
        // метод предназначен в первую очередь для проверки параметров в методах и конструкторах с несколькими параметрами
        Objects.requireNonNull(entity, "Entity must not be null");
        if(entity.isNew()) {
            entity.setId(counter.incrementAndGet());
            map.put(entity.getId(), entity);
            return entity;
        }
        // computeIfPresent
        // если элемент с ключом key существует - выполняем функцию
        return map.computeIfPresent(entity.getId(), ((integer, t) -> entity));
    }

    public boolean delete(int id) {
        if(map.remove(id) != null) {
            return true;
        } else return false;
    }

    public T get(int id) {
        return map.get(id);
    }

    Collection<T> getCollection() {
        return map.values();
    }

    void put(T entity) {
        Objects.requireNonNull(entity, "Entity must not be null");
        map.put(entity.id(), entity);
    }
}
