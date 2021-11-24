package ru.alex.util;

import ru.alex.model.AbstractBaseEntity;
import ru.alex.util.exception.NotFoundException;

public class ValidationUtil {

    private ValidationUtil() {}

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    private static void checkNotFound(boolean found, String msg) {
        // found == false
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id" + id);
    }

    // проверяет, что entity, которое пришло в контроллер,
    // **сохранем новую сущность  и проверяем, что действительно ее id=null
    // действительно имеет нулевоой id-шник
    // если не нулевой, то это exception
    public static void checkNew(AbstractBaseEntity entity) {
        // entity.isNew() == false
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null");
        }
    }

    // убедиться, что идентификатор согласован
    // проверяем согласованность данных, то есть
    // сущность, которую мы обновляем должна быть передана или с нулевым id (entity.isNew())
    // тогда мы просто присваиваем ей id, который передан в параметрах
    // либо он должен совпадать с параметром id, иначе ошибка
    // http://stackoverflow.com/a/32728226/548473
    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }
}
