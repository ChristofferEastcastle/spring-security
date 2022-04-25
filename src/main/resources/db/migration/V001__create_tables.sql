create sequence hibernate_sequence start 1;

create table animals(
    id serial primary key,
    name varchar(50),
    type varchar(20),
    age int,
    health int
);

create table users(
    id serial primary key,
    username varchar(50) unique,
    password varchar(100),
    enabled boolean
);

create table authorities(
    id serial primary key,
    name varchar(20) unique
);

create table users_authorities(
    user_entity_id int references users(id),
    authorities_id int references authorities(id)
);