INSERT INTO authors (name, bio) VALUES ('J.K. Rowling', 'Британская писательница, известная своими книгами о Гарри Поттере.');
INSERT INTO authors (name, bio) VALUES ('Stephen King', 'Американский писатель, специализирующийся на жанре ужасов.');
INSERT INTO authors (name, bio) VALUES ('Peter Straub', 'Американский писатель, известный своими романами в жанре хоррор и фэнтези.');

INSERT INTO books (title, description) VALUES ('Harry Potter and the Philosopher''s Stone', 'Первая книга о приключениях Гарри Поттера.');
INSERT INTO books (title, description) VALUES ('The Shining', 'Классический роман ужасов.');
INSERT INTO books (title, description) VALUES ('The Talisman', 'Совместный роман Стивена Кинга и Питера Страуба о параллельных мирах.');

INSERT INTO book_authors (book_id, author_id) VALUES (1, 1);
INSERT INTO book_authors (book_id, author_id) VALUES (2, 2);
INSERT INTO book_authors (book_id, author_id) VALUES (3, 2);
INSERT INTO book_authors (book_id, author_id) VALUES (3, 3);