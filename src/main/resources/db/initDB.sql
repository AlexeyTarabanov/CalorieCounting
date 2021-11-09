-- если есть таблицы - я их дропаю
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
-- дропаю сиквенсы
-- Sequences в PostgreSQL - генераторы уникальных последовательностей чисел,
-- использовать можно аналогичным auto_increment полям образом в MySQL.
DROP SEQUENCE IF EXISTS global_seq;

-- создаю уникальный SEQUENCE
CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
-- это означает, что id PRIMARY KEY будет генерить автоматически за счет того, что
-- будет спрашивать следющее значение у последовательности global_seq
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
-- здесь говорим, что у нас уникальный email - мы не сможем создать 2-ух пользователей с одним и тем же email-om
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    -- реляционная база данных
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    -- UNIQUE Constraint — все значения в столбце должны быть уникальными
    -- у одного и того же user'a не может быть 2-ух одинаковых ролей
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    -- указываем, что роль привязана к user-у
    -- ON DELETE CASCADE - при удалении юзера все его роли удаляются
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);