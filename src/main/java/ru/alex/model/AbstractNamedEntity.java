package ru.alex.model;

/**
 * AbstractNamedEntity -
 * от него можно наследовать все сущности у которых есть имя
 * */

public abstract class AbstractNamedEntity extends AbstractBaseEntity{

    protected String name;

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + name + ")";
    }
}
