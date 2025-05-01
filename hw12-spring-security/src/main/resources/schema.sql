create table if not exists authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table if not exists genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table if not exists books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table if not exists comments
(
    id        bigserial,
    book_id   bigint references books (id) on delete cascade,
    comment   varchar(255),
    primary key (id)
);

create table if not exists users
(
    id         bigserial,
    user_name  varchar(255),
    password   varchar(255),
    full_name  varchar(255),
    street     varchar(255),
    city       varchar(255),
    phone      varchar(255),
    primary key (id)
);