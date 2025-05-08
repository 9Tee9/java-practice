INSERT INTO authors (name, bio) VALUES ('J.K. Rowling', 'Британская писательница, известная своими книгами о Гарри Поттере.');
INSERT INTO authors (name, bio) VALUES ('Stephen King', 'Американский писатель, специализирующийся на жанре ужасов.');
INSERT INTO authors (name, bio) VALUES ('Peter Straub', 'Американский писатель, известный своими романами в жанре хоррор и фэнтези.');

INSERT INTO books (title, description) VALUES ('The Shining', 'Классический роман ужасов.');
INSERT INTO books (title, description) VALUES ('The Talisman', 'Совместный роман Стивена Кинга и Питера Страуба о параллельных мирах.');
INSERT INTO books (title, description) VALUES ('Harry Potter and the Philosopher''s Stone', 'Первая книга о приключениях Гарри Поттера.');

INSERT INTO book_authors (book_id, author_id) VALUES (1, 1);
INSERT INTO book_authors (book_id, author_id) VALUES (2, 2);
INSERT INTO book_authors (book_id, author_id) VALUES (3, 2);
INSERT INTO book_authors (book_id, author_id) VALUES (3, 3);

INSERT INTO users (username, password, role, is_account_non_locked, failed_attempts) VALUES ('user1', '$2a$10$ntppU9xAYFVuaocnAxtWWuguDhQq4zmdX03zUu2ALI6TWVT39yo/q', 'ROLE_USER', true, 0);
INSERT INTO users (username, password, role, is_account_non_locked, failed_attempts) VALUES ('moderator1', '$2a$10$zxOV5jhRsC54gidl1OZ3aen8DwWhCQBWjUN.RyFbtL.bOcyXyJCaW', 'ROLE_MODERATOR', true, 0);
INSERT INTO users (username, password, role, is_account_non_locked, failed_attempts) VALUES ('admin1', '$2a$10$.dui.4vqQHZT9RXHFDn0heUqHmdxTjyOt4pVyNca.eJ9EyG3MwVfC', 'ROLE_SUPER_ADMIN', true, 0);

--пароли
--"user_password"; // Пароль для user1
--"moderator_password"; // Пароль для moderator1
--"admin_password"; // Пароль для admin1

