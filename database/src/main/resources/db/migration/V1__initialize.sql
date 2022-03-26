DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles
(
    id   serial PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE,
    description varchar(255) NULL,
    CONSTRAINT UK_role_name unique (name)
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id         serial PRIMARY KEY,
    password   varchar(255) NOT NULL,
    email      varchar(128) NOT NULL UNIQUE,
    phone       varchar(128)  null,
    first_name  varchar(128)  null,
    middle_name varchar(128)  null,
    last_name   varchar(128)  null,
    dt_create  timestamp   NOT NULL DEFAULT NOW(),
    dt_modify  timestamp   NOT NULL DEFAULT NOW(),
    CONSTRAINT UK_user_email UNIQUE (email)
);

DROP TABLE IF EXISTS users_roles CASCADE;
CREATE TABLE users_roles
(
    user_id bigint NOT NULL,
    role_id int    NOT NULL,
    CONSTRAINT UC_user_role UNIQUE (user_id, role_id),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_users_roles_user_id
        FOREIGN KEY (user_id) references users (id),
    CONSTRAINT FK_users_roles_role_id
        FOREIGN KEY (role_id) references roles (id)
);


INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');
