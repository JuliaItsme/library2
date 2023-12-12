TRUNCATE person RESTART IDENTITY CASCADE ;

INSERT INTO Person (full_name, year_of_birth)
VALUES ('Olga R', 1993),
       ('Mikhail', 2016),
       ('Valentina', 1927);

INSERT INTO Book (title, author, year_of_production)
VALUES ('Микросервисы Spring в действии', 'Джон Карнелл', 2022),
       ('Spring в действии', 'Крейг Уоллс', 2022),
       ('Философия Java', 'Брюс Эккель', 2017),
       ('Java persistence API и Hibernate', 'кристиан Бауэр', 2017),
       ('Java эффективное программирование', 'Джошуа Блох', 2019);

UPDATE Book SET take_at='2023-11-07 08:00:00' WHERE id=1;