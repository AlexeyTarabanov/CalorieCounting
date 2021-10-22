package ru.alex.model;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static ru.alex.util.UserMealsUtil.DEFAULT_CALORIES_PER_DAY;

public class User extends AbstractNamedEntity {

    private String email;
    private String password;
    // Enabled переводится как включено, активировано, разрешено – указывает на задействование функции
    private boolean enabled = true;

    // абсолютный момент времени (в отличии от приема еды),
    // поэтому дата не LocalDate, а именно Date()
    private Date registered = new Date();
    // так как у пользователя мб несколько ролей, поэтому Se
    private Set<Role> roles;
    private int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;


    // EnumSet — это реализация наборов Set для работы с типом enum.
    // Он не позволяет пользователю добавлять значения NULL и создает исключение NullPointerException
    // Элементы хранятся в порядке их сохранения
    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Set<Role> roles) {
        super(id, name);
        this.name = name;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
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
