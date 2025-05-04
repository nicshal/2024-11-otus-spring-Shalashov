insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into users(user_name, password, full_name, street, city, phone, role)
values ('admin', '$2a$08$NqWBwEDEw5uxzDTRZUAT5OgUz9JDwlngRxdpJbWNMvSUok4ZlqgB.', 'Boris', 'First Street', 'Moscow', '777-777', 'ADMIN');
