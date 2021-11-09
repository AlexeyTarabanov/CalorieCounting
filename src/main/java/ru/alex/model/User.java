package ru.alex.model;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static ru.alex.util.UserMealsUtil.DEFAULT_CALORIES_PER_DAY;

public class User extends AbstractNamedEntity {

    private String email;
    private String password;
    // enabled переводится как включено, активировано, разрешено –
    // указывает на задействование функции
    private boolean enabled = true;

    // абсолютный момент времени (в отличии от приема еды),
    // поэтому дата не LocalDate, а именно Date()
    private Date registered = new Date();
    // так как у пользователя мб несколько ролей, поэтому Se
    private Set<Role> roles;
    private int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public User() {
    }

    // конструктор копирования
    // замена методу clone()
    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.caloriesPerDay, u.enabled, u.registered, u.roles);
    }

    // EnumSet — это реализация наборов Set для работы с типом enum.
    // Он не позволяет пользователю добавлять значения NULL и создает исключение NullPointerException
    // Элементы хранятся в порядке их сохранения
    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, new Date(), EnumSet.of(role, roles));
    }

    // в конструктор User внес registered и делаю копию roles, чтобы роли нельзя было изменить после инициализации.
    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        // EnumSet.noneOf(Role.class)
        // создает пустой набор перечислений с указанным типом элемента
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}
