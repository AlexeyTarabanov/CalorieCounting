package ru.alex.model;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 AbstractBaseEntity будет хранить primary key,
 чтобы не дублировать в каждую сущность.
 primary key - уникальный идентификатор
 */

public abstract class AbstractBaseEntity {

    public static final int START_SEQ = 100000;

    protected Integer id;

    public AbstractBaseEntity() {}

    protected AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public int id() {
        // утверждают, что объект не равен нулю
        Assert.notNull(id, "Entity must have id");
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}
