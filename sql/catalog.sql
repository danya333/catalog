-- Схема для хранения товаров и характеристик товаров специфических для каждой
-- отдельно взятой категории.

-- В схеме должна быть возможность хранения категорий у каждой из которых может
-- быть свой перечень характеристик, например категория `Процессоры` с
-- характеристиками `Производитель`, `Количество ядер`, `Сокет` или категория
-- `Мониторы` с характеристиками `Производитель`, `Диагональ`, `Матрица`,
-- `Разрешение`.

-- Процессоры      -> Intel Core I9 9900 -> AMD Ryzen R7 7700
-- Производитель   -> Intel              -> AMD
-- Количество ядер -> 8                  -> 12
-- Сокет           -> 1250               -> AM4

-- Мониторы      -> Samsung SU556270 -> AOC Z215S659
-- Производитель -> Samsung          -> AOC
-- Диагональ     -> 27               -> 21.5
-- Матрица       -> TN               -> AH-IPS
-- Разрешение    -> 2560*1440        -> 1920*1080

drop table if exists categories cascade;
-- drop table if exists products;
-- drop table if exists descriptions;
-- drop table if exists specifications;

create table categories
(
    id   serial8,
    name varchar not null,
    primary key (id)
);

create table products
(
    id          serial8,
    category_id int8    not null,
    name        varchar not null,
    price       int4 not null,
    primary key (id),
    foreign key (category_id) references categories (id)
);



create table specifications
(
    id          serial8,
    category_id int8 not null,
    name        varchar,
    primary key (id),
    foreign key (category_id) references categories (id)
);

create table descriptions
(
    id               serial8,
    specification_id int8 not null,
    product_id       int8 not null,
    name            varchar,
    primary key (id),
    foreign key (specification_id) references specifications (id),
    foreign key (product_id) references products (id)
);

insert into categories (name)
values ('Процессоры'),
       ('Мониторы');

insert into products (category_id, name, price)
values (1, 'Intel Core I9 9900', 250000),
       (1, 'AMD Ryzen R7 7700', 270000),
       (2, 'Samsung SU556270', 150000),
       (2, 'AOC Z215S659', 180000);

insert into specifications (category_id, name)
values (1, 'Производитель'),
       (1, 'Количество ядер'),
       (1, 'Сокет'),
       (2, 'Производитель'),
       (2, 'Диагональ'),
       (2, 'Матрица'),
       (2, 'Разрешение');

insert into descriptions (specification_id, product_id,  name)
values (1, 1, 'Intel'),
       (1, 2, 'AMD'),
       (2, 1, '8'),
       (2, 2, '12'),
       (3, 1, '1250'),
       (3, 2, 'AM4'),
       (4, 3, 'Samsung'),
       (4, 4, 'AOC'),
       (5, 3, '27'),
       (5, 4, '21.5'),
       (6, 3, 'TN'),
       (6, 4, 'AH-IPS'),
       (7, 3, '2560*1440'),
       (7, 4, '1920*1080');



-- Необходимо будет вывести характеристики и их значения определенного товара основываясь на его id.

-- 2
-- Производитель AMD
-- Сокет AM4

select s.name, d.name
from specifications as s
inner join  descriptions d on s.id = d.specification_id
where d.product_id = 3

