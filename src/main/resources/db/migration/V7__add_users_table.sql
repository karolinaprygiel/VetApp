create table users
(
    id       serial primary key,
    active   bool default true,
    password varchar(255),
    salt     varchar(255),
    role     varchar(127),
    username varchar(127) not null
);

create unique index users_username_uindex
    on users (username);