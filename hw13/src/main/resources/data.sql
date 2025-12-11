insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1),
       ('BookTitle_2', 2, 2),
       ('BookTitle_3', 3, 3);

insert into comments(book_id, text)
values (1,'Comment_1'),
       (2, 'Comment_2'),
       (3, 'Comment_3'),
       (1, 'Comment_4');

insert into roles (name)
values ('USER'), ('ADMIN');

insert into users(username, password, role_id)
values ('user', '$2a$10$RQ2BRK5oIsobxSUa/PVMyOqiK2kcFczj8vE/2VilcBaled2fclIq.', 1),
       ('admin', '$2a$10$EXvc3B0tSzxmFTJF98FbFut9SZchbWdJDxORXh7MGPfLwrFGPjTvq', 2);
